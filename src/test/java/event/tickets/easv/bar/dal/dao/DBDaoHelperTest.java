package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.dal.database.DBConnector;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import event.tickets.easv.bar.util.Result.Failure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DBDaoHelperTest {
    private static final String PATH = "src/test/java/event/tickets/easv/bar/dal/database/";

    private static final String TEST_DB_CONFIG = PATH + "dbconfig.test.properties";

    private static final String EMPTY_DB_SETUP = PATH + "test_dbsetup.sql";
    private static final String POPULATE_SINGLE = PATH + "test_dbsetup_populate_single.sql";
    private static final String POPULATE_MULTIPLE = PATH + "test_dbsetup_populate_multiple.sql";
    private static final String POPULATE_BOUNDARY_CONDITIONS = PATH + "test_dbsetup_populate_boundary_conditions.sql";
    private static final String POPULATE_INVALID_DATA = PATH + "test_dbsetup_populate_invalid_data.sql";

    private DBConnector dbConnector;
    private DBDaoHelper<Event> daoHelper;

    @BeforeEach
    void setup() {
        dbConnector = null;
        try {
            dbConnector = new DBConnector(TEST_DB_CONFIG);
        } catch (IOException e) {
            throw new RuntimeException("Error trying to read TEST_DB_CONFIG_PATH in DBDaoHelperTest.setup().\n " + e);
        }

        daoHelper = new DBDaoHelper<>(new EventSQLTemplate(), new EventResultSetMapper());
        daoHelper.setDbConnector(dbConnector);
        runScript(EMPTY_DB_SETUP);
    }

    private void runScript(String path) {
        String script;
        try {
            script = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            throw new RuntimeException("Error trying to read " + path + " in DBDaoHelperTest.runScript().\n " + e);
        }

        String[] statements = script.split("GO");

        try (Connection conn = dbConnector.connection();
             Statement stmt = conn.createStatement()) {
            for (String statement : statements) {
                if (!statement.trim().isEmpty()) {
                    stmt.execute(statement);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error trying to connect to TEST DATABASE in DBDaoHelperTest.runScript().\n " + e);
        }
    }

    @Test
    void allEventEmptyTable() {
        // Call
        Result<List<Event>> result = daoHelper.all();

        // Check
        assertThat(result).isInstanceOf(Success.class);
        var success = (Success<List<Event>>) result;
        assertThat(success.result()).isEmpty();
    }

    @Test
    void allEventSingleEvent() {
        // Setup
        runScript(POPULATE_SINGLE);

        // Call
        Result<List<Event>> result = daoHelper.all();

        // Check
        assertThat(result).isInstanceOf(Success.class);
        var success = (Success<List<Event>>) result;
        assertThat(success.result()).hasSize(1);
        assertThat(success.result().getFirst()).isEqualTo(new Event(1, "Single"));
    }

    @Test
    void allEventMultipleEvents() {
        // Setup
        runScript(POPULATE_MULTIPLE);

        // Call
        Result<List<Event>> result = daoHelper.all();

        // Check
        assertThat(result).isInstanceOf(Success.class);
        var success = (Success<List<Event>>) result;
        assertThat(success.result()).hasSize(20);
    }

    @Test
    void allEventDataIntegrity() {
        // Setup
        runScript(POPULATE_MULTIPLE);

        // Call
        Result<List<Event>> result = daoHelper.all();

        // Check
        assertThat(result).isInstanceOf(Success.class);
        var success = (Success<List<Event>>) result;
        var events = List.of(
                new Event(1, "International Food Festival"),
                new Event(2, "Vegan Cooking Workshop"),
                new Event(3, "Farm to Table Dinner"),
                new Event(4, "Wine and Cheese Night"),
                new Event(5, "Italian Pasta Making Class"),
                new Event(6, "French Cuisine Tasting"),
                new Event(7, "Sushi Rolling Workshop"),
                new Event(8, "Chocolate Making Class"),
                new Event(9, "BBQ and Grill Cook-off"),
                new Event(10, "Farmers Market Tour"),
                new Event(11, "Pastry and Baking Workshop"),
                new Event(12,"Coffee Tasting Experience"),
                new Event(13, "Beer Brewing Demonstration"),
                new Event(14, "Gourmet Burger Festival"),
                new Event(15, "Mexican Fiesta Night"),
                new Event(16, "Street Food Extravaganza"),
                new Event(17, "Ice Cream Social"),
                new Event(18, "Pizza Making Party"),
                new Event(19, "Seafood Feast"),
                new Event(20, "Culinary Arts Festival")
        );
        assertThat(success.result()).isEqualTo(events);
    }

    @Test
    void allEventBoundaryConditions() {
        // Setup
        runScript(POPULATE_BOUNDARY_CONDITIONS);

        // Call
        Result<List<Event>> result = daoHelper.all();

        // Check
        assertThat(result).isInstanceOf(Success.class);
        var success = (Success<List<Event>>) result;
        var events = List.of(
                new Event(1, "A"),
                new Event(2, "A".repeat(255))
        );
        assertThat(success.result()).isEqualTo(events);
    }

    @Test
    void allEventInvalidData() {
        // Setup
        runScript(POPULATE_INVALID_DATA);

        // Call & Check
        assertThrows(IllegalArgumentException.class, () -> daoHelper.all());
    }

    @Test
    void allEventDBConnectionFailure() throws SQLException {
        // Setup
        var mockDbConnector = mock(DBConnector.class);
        when(mockDbConnector.connection()).thenThrow(new SQLException("Connection failed"));
        daoHelper.setDbConnector(mockDbConnector);

        // Call
        Result<List<Event>> result = daoHelper.all();

        // Check
        assertThat(result).isInstanceOf(Failure.class);
    }
}