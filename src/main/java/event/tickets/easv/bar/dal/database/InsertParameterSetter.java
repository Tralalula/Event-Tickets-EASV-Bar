package event.tickets.easv.bar.dal.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface InsertParameterSetter<T> {
    void setParameters(PreparedStatement stmt, T entity) throws SQLException;
}
