package services;

import java.util.List;
import java.util.Optional;

public interface Service<T, K> {
    List<T> getAll();
    Optional<T> get(K id);
    void create(T obj);
    void update(T obj);
    void delete(K id);

}
