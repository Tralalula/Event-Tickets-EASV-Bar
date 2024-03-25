package event.tickets.easv.bar.dal.database.common;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOHelper<T> {
    private final DBConnector dbConnector;
    private final SQLTemplate<T> sqlTemplate;
    private final ResultSetMapper<T> resultSetMapper;

    public DAOHelper(SQLTemplate<T> sqlTemplate, ResultSetMapper<T> resultSetMapper) throws IOException {
        this.dbConnector = new DBConnector();
        this.sqlTemplate = sqlTemplate;
        this.resultSetMapper = resultSetMapper;
    }

    public List<T> all() throws SQLException {
        List<T> results = new ArrayList<>();
        String sql = sqlTemplate.getSelectSQL();

        try (Connection conn = dbConnector.connection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                results.add(resultSetMapper.map(rs));
            }
        }

        return results;
    }
}
