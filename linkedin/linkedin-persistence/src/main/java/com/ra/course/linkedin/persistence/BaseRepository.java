package com.ra.course.linkedin.persistence;

import com.ra.course.linkedin.model.entity.BaseEntity;

import java.util.Optional;

public interface BaseRepository<T extends BaseEntity> {
    Optional<T> getById(Long id);

    T save(T entity);

    void delete(T entity);

    Iterable<T> getAll();
}
