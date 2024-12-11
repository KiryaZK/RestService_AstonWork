package dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T, K> {
    Optional<T> get(K id);
    List<T> getAll();
    void create(T obj);
    void update(T obj);
    void delete(K id);

}
