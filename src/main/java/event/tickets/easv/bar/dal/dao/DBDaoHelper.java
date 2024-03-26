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
     * Retrieves all entities of type T from the database.
     *
     * @return a {@code Result<List<T>>} which is either a Success containing the list of entities,
     *         or a Failure if any issues occur during data retrieval.
     */
    public Result<List<T>> all() {
        try {
            setupDBConnector();
        } catch (IOException e) {
            return new Failure<>(FailureType.IO_FAILURE, "Failed to read from the data source", e);
        }

        List<T> results = new ArrayList<>();
        String sql = sqlTemplate.getSelectSQL();

        try (Connection conn = dbConnector.connection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                results.add(resultSetMapper.map(rs));
            }
        } catch (SQLException e) {
            return new Failure<>(FailureType.DB_DATA_RETRIEVAL_FAILURE, "Failed to retrieve data from the database", e);
        }

        return new Success<>(results);
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
