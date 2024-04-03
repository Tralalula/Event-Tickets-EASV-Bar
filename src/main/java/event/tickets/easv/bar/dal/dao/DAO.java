package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.util.Result;

import java.util.List;
import java.util.Optional;

/**
 * DAO (Data Access Object) represents a generic interface for performing CRUD (Create, Read, Update, Delete)
 * operations on entities of type T.<br>
 *
 * @param <T> the type of the entity that the DAO will manage.
 */
public interface DAO<T> {
    /**
     * Retrieves an entity by its identifier.
     *
     * @param id the unique identifier of the entity to retrieve.
     * @return a result containing an optional containing the retrieved entity; otherwise an empty Optional.
     */
    Result<Optional<T>> get(int id);

    /**
     * Retrieves all entities of type T.
     *
     * @return a result containing a list of all entities of type T found.
     *
     */
    Result<List<T>> all();

    /**
     * Adds a new entity to the data source.
     *
     * @param entity the entity to add.
     * @return the added entity.
     */
    Result<T> add(T entity);

    Result<List<T>> addAll(List<T> entities);

    Result<Integer> batchAdd(List<T> entities);

    /**
     * Updates an existing entity with new data.
     *
     * @param original the original entity to update.
     * @param updatedData the entity containing the updated data.
     * @return true if the update was successful; false otherwise.
     */
    Result<Boolean> update(T original, T updatedData);

    /**
     * Deletes an entity from the data source.
     *
     * @param entity the entity to delete.
     * @return true if the deletion was successful; false otherwise.
     */
    Result<Boolean> delete(T entity);
}
