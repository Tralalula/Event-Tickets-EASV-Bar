package event.tickets.easv.bar.dal.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface AssociationInsertParameterSetter<A, B> {
    void setParameters(PreparedStatement stmt, A entityA, B entityB) throws SQLException;
}
