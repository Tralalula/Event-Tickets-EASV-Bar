package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.dal.database.DBConnector;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
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

class DBDaoHelperTest {
    private static final String PATH = "src/test/java/event/tickets/easv/bar/dal/database/";

    private static final String TEST_DB_CONFIG_PATH = PATH + "dbconfig.test.properties";

    private static final String EMPTY_DB_SETUP = PATH + "test_dbsetup.sql";
    private static final String POPULATE_SINGLE = PATH + "test_dbsetup_populate_single.sql";

    private DBConnector dbConnector;
    private DBDaoHelper<Event> daoHelper;

    @BeforeEach
    void setup() {
        dbConnector = null;
        try {
            dbConnector = new DBConnector(TEST_DB_CONFIG_PATH);
        } catch (IOException e) {
            throw new RuntimeException("Error trying to read TEST_DB_CONFIG_PATH in DBDaoHelperTest.setup().\n " + e);
        }

        daoHelper = new DBDaoHelper<>(new EventSQLTemplate(), new EventResultSetMapper());
        daoHelper.setDbConnector(dbConnector);
        runScript(EMPTY_DB_SETUP);
    }

    private void runScript(String path) {
        String script = null;
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
}