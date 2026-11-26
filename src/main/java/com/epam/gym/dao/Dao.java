package com.epam.gym.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    T save(T entity);

    Optional<T> findById(Long id);

    List<T> findAll();
}
