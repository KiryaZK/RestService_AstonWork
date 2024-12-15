package services;

import java.util.List;
import java.util.Optional;

/**
 * The Service interface defines the contract for services that handle operations
 * related to T objects. Implementing classes must provide functionality for creating,
 * retrieving, updating, and deleting T entities.
 */
public interface Service<T, K> {
    /**
     * Retrieves a list of all T objects available in the system.
     *
     * @return A list of DTO objects representing all T type.
     */
    List<T> getAll();
    /**
     * Retrieves a T type by its unique identifier.
     *
     * @param id The unique identifier of the T type.
     * @return An Optional containing the DTO if found, or an empty Optional if not found.
     */
    Optional<T> get(K id);
    /**
     * Creates a new T type in the system.
     *
     * @param obj The DTO object representing the T type to be created.
     */
    void create(T obj);
    /**
     * Updates an existing T type in the system.
     *
     * @param obj The DTO object representing the T type to be updated.
     */
    void update(T obj);
    /**
     * Deletes a T type from the system by its unique identifier.
     *
     * @param id The unique identifier of the T type to be deleted.
     */
    void delete(K id);

}
