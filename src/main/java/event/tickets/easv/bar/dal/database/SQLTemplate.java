package event.tickets.easv.bar.dal.database;

/**
 * Provides a template for constructing SQL query strings specific to the entity type T.
 */
public interface SQLTemplate<T> {
    /**
     * Produces the SQL SELECT query string.
     *
     * @return a String containing a SQL SELECT query tailored to the entity type T.
     */
    String getSelectSQL();

    String allSelectSQL();

    String insertSQL();
}
