package event.tickets.easv.bar.dal.database.common;

import event.tickets.easv.bar.util.Result;

import java.util.List;
import java.util.Optional;

/**
 * DAO (Data Access Object) represents a generic interface for performing CRUD (Create, Read, Update, Delete)
 * operations on a collection of entities of type T.<br>
 * <br>
 * All methods throw an Exception if a problem occurs during data access.
 *
 * @param <T> the type of the entity that the DAO will manage.
 */
public interface DAO<T> {
    /**
     * Retrieves an entity by its identifier.
     *
     * @param id the unique identifier of the entity to retrieve.
     * @return an optional containing the retrieved entity; otherwise an empty Optional.
     */
    Optional<T> get(int id) throws Exception;

    /**
     * Retrieves all entities of type T.
     *
     * @return a list containing all entities of type T found; the list can be empty.
     */
    Result<List<T>> all();

    /**
     * Adds a new entity to the data source.
     *
     * @param t the entity to add.
     * @return the added entity.
     */
    T add(T t) throws Exception;

    /**
     * Updates an existing entity with new data.
     *
     * @param original the original entity to update.
     * @param updatedData the entity containing the updated data.
     * @return true if the update was successful; false otherwise.
     */
    boolean update(T original, T updatedData) throws Exception;

    /**
     * Deletes an entity from the data source.
     *
     * @param t the entity to delete.
     * @return true if the deletion was successful; false otherwise.
     */
    boolean delete(T t) throws Exception;
}
