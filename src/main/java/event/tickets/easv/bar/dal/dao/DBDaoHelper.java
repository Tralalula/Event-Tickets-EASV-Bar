package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.dal.database.DBConnector;
import event.tickets.easv.bar.dal.database.ResultSetMapper;
import event.tickets.easv.bar.dal.database.SQLTemplate;
import event.tickets.easv.bar.util.FailureType;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import event.tickets.easv.bar.util.Result.Failure;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Helper class providing generic data access operations against a database.
 */
public class DBDaoHelper<T> {
    private DBConnector dbConnector = null;
    private final SQLTemplate<T> sqlTemplate;
    private final ResultSetMapper<T> resultSetMapper;

    /**
     * Constructs a new DBDaoHelper that provides helper methods for data access operations against a database.
     *
     * @param sqlTemplate the template for generic SQL queries.
     * @param resultSetMapper the mapper for converting ResultSet rows into entities of type T.
     */
    public DBDaoHelper(SQLTemplate<T> sqlTemplate, ResultSetMapper<T> resultSetMapper) {
        this.sqlTemplate = sqlTemplate;
        this.resultSetMapper = resultSetMapper;
    }

    /**
     * Retrieves an entity by its identifier.
     *
     * @param id the unique identifier of the entity to retrieve.
     * @return a result which is either a success or a failure if any issues occurred during data retrieval.
     *         The success contains optional which either contains the entity if found; otherwise an empty optional.
     */
    public Result<Optional<T>> get(int id) {
        try {
            setupDBConnector();
        } catch (IOException e) {
            return Failure.of(FailureType.IO_FAILURE, "Failed to read from the data source", e);
        }

        String sql = sqlTemplate.getSelectSQL();

        try (Connection conn = dbConnector.connection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Success.of(Optional.of(resultSetMapper.map(rs)));
            } else {
                return Success.of(Optional.empty());
            }
        } catch (SQLException e) {
            return Failure.of(FailureType.DB_DATA_RETRIEVAL_FAILURE, "Failed to retrieve data from the database", e);
        }
    }

    /**
     * Retrieves all entities of type T from the database.
     *
     * @return a {@code Result<List<T>>} which is either a Success containing the list of entities,
     *         or a Failure if any issues occur during data retrieval.
     */
    public Result<List<T>> all() {
        try {
            setupDBConnector();
        } catch (IOException e) {
            return Failure.of(FailureType.IO_FAILURE, "Failed to read from the data source", e);
        }

        List<T> results = new ArrayList<>();
        String sql = sqlTemplate.allSelectSQL();

        try (Connection conn = dbConnector.connection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                results.add(resultSetMapper.map(rs));
            }
        } catch (SQLException e) {
            return Failure.of(FailureType.DB_DATA_RETRIEVAL_FAILURE, "Failed to retrieve data from the database", e);
        }

        return Success.of(results);
    }

    /**
     * Should be called internally by every method that tries to perform database operations.
     */
    private void setupDBConnector() throws IOException {
        if (dbConnector == null) {
            dbConnector = new DBConnector();
        }
    }

    /**
     * To set DBConnector for testing purposes.
     */
    void setDbConnector(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }
}
