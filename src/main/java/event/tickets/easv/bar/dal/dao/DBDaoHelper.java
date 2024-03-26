package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.dal.database.DBConnector;
import event.tickets.easv.bar.dal.database.ResultSetMapper;
import event.tickets.easv.bar.dal.database.SQLTemplate;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import event.tickets.easv.bar.util.Result.Failure;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBDaoHelper<T> {
    private DBConnector dbConnector = null;
    private final SQLTemplate<T> sqlTemplate;
    private final ResultSetMapper<T> resultSetMapper;

    public DBDaoHelper(SQLTemplate<T> sqlTemplate, ResultSetMapper<T> resultSetMapper) {
        this.sqlTemplate = sqlTemplate;
        this.resultSetMapper = resultSetMapper;
    }

    public Result<List<T>> all() {
        try {
            setupDBConnector();
        } catch (IOException e) {
            return new Failure<>(e);
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
            return new Failure<>(e);
        }

        return new Success<>(results);
    }

    private void setupDBConnector() throws IOException {
        if (dbConnector == null) {
            dbConnector = new DBConnector();
        }
    }

    void setDbConnector(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }
}
