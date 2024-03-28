package event.tickets.easv.bar.dal.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface UpdateParameterSetter<T> {
    void setParameters(PreparedStatement stmt, T original, T updatedData) throws SQLException;
}
