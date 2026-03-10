package service;

import java.util.List;

public interface IBaseService<T, ID> {
    boolean add(T t);

    boolean update(T t);

    boolean delete(ID id);

    T findById(ID id);

    List<T> findAll();
}
