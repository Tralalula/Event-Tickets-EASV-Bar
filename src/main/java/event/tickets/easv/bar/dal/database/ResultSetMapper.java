package event.tickets.easv.bar.dal.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.jetbrains.annotations.NotNull;

public interface ResultSetMapper<T> {
    /**
     * Consumes a result set and produces a corresponding entity
     *
     * @param rs the ResultSet to be consumed. Assumed to be positioned at the correct row for mapping.
     *           Must not be null.
     * @return an entity to be produced from the ResultSet's current row data.
     * @throws SQLException if accessing the ResultSet data results in an SQL error.
     */
    T map(@NotNull ResultSet rs) throws SQLException;
}