package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.dal.database.DBConnector;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Failure;
import event.tickets.easv.bar.util.Result.Success;
import org.checkerframework.checker.units.qual.N;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DBJunctionDAOHelperTest {
    private static final String PATH = "src/test/java/event/tickets/easv/bar/dal/database/";

    private static final String TEST_DB_CONFIG = PATH + "dbconfig.test.properties";

    private static final String EMPTY_DB_SETUP = PATH + "test_dbsetup.sql";
    private static final String POPULATE_SINGLE = PATH + "test_dbsetup_populate_single.sql";
    private static final String POPULATE_MULTIPLE = PATH + "test_dbsetup_populate_multiple.sql";
    private static final String POPULATE_BOUNDARY_CONDITIONS = PATH + "test_dbsetup_populate_boundary_conditions.sql";
    private static final String POPULATE_INVALID_DATA = PATH + "test_dbsetup_populate_invalid_data.sql";

    private DBConnector dbConnector;
    private DBJunctionDAOHelper<Event, User> eventUserDaoHelper;

    @BeforeEach
    void setup() {
        dbConnector = null;
        try {
            dbConnector = new DBConnector(TEST_DB_CONFIG);
        } catch (IOException e) {
            throw new RuntimeException("Error trying to read TEST_DB_CONFIG_PATH in DBDaoHelperTest.setup().\n " + e);
        }

        this.eventUserDaoHelper = new DBJunctionDAOHelper<>(
                new EventUserSQLTemplate(),
                new EventUserInsertParameterSetter()
        );
        eventUserDaoHelper.setDbConnector(dbConnector);

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

    @Nested
    class EventUserTest {
        @Test
        void addAssociationEventUser() {
            // Setup
            runScript(POPULATE_SINGLE);
            var event = new Event(1, "Single", "sample.png", "6700 Esbjerg", LocalDate.of(2024, 4, 5), LocalDate.of(2024, 4, 5), LocalTime.of(10, 0), LocalTime.of(20, 0), "", "");
            var user = new User(1, "test");

            // Call
            Result<Boolean> result = eventUserDaoHelper.addAssociation(event, user);

            // Check
            assertThat(result).isInstanceOf(Success.class);
            var success = (Success<Boolean>) result;
            assertThat(success.result()).isEqualTo(true);
        }

        @Test
        void addAssociationEventUserDBConnectionFailure() throws SQLException {
            // Setup
            var mockDbConnector = mock(DBConnector.class);
            when(mockDbConnector.connection()).thenThrow(new SQLException("Connection failed"));
            eventUserDaoHelper.setDbConnector(mockDbConnector);
            var event = new Event(1, "Single", "sample.png", "6700 Esbjerg", LocalDate.of(2024, 4, 5), LocalDate.of(2024, 4, 5), LocalTime.of(10, 0), LocalTime.of(20, 0), "", "");
            var user = new User(1, "test");

            // Call
            Result<Boolean> result = eventUserDaoHelper.addAssociation(event, user);

            // Check
            assertThat(result).isInstanceOf(Failure.class);
        }
    }
}