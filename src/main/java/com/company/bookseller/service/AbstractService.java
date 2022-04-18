package com.company.bookseller.service;

import java.util.List;

public interface AbstractService<T, K> {
    List<T> getAll();

    T get(K id);

    T create(T entity);

    T update(T entity);

    void delete(K id);
}