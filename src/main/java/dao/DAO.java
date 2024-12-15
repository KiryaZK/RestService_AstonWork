package dao;

import java.util.List;
import java.util.Optional;

/**
 * The Dao interface defines the contract for data access operations related to entities.
 * Implementing classes must provide methods to retrieve, create, update, and delete entities in the
 * underlying data store.
 */
public interface DAO<T, K> {
    /**
     * Retrieves a T object by its unique identifier.
     *
     * @param id The unique identifier of the T object.
     * @return An Optional containing the T object if found, or an empty Optional if not found.
     */
    Optional<T> get(K id);
    /**
     * Retrieves a list of all T objects available in the data store.
     *
     * @return A list of T objects, or an empty list if no T objects are found.
     */
    List<T> getAll();
    /**
     * Creates a new T object in the data store.
     *
     * @param obj The T object entity to be created.
     */
    void create(T obj);
    /**
     * Updates an existing T object in the data store.
     *
     * @param obj The T object entity to be updated.
     */
    void update(T obj);
    /**
     * Deletes a T object from the data store by its unique identifier.
     *
     * @param id The unique identifier of the T object to be deleted.
     */
    void delete(K id);

}
