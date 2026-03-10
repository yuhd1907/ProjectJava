package service;

import java.util.List;

public interface IBaseService<T, ID> {
    void add(T t);

    void update(T t);

    void delete(ID id);

    T findById(ID id);

    List<T> findAll();
}
