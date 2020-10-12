package com.ra.course.linkedin.persistence;

import com.ra.course.linkedin.model.entity.BaseEntity;
import com.ra.course.linkedin.model.entity.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class BaseRepositoryImpl<T extends BaseEntity> implements BaseRepository<T> {

    private transient long currentId = 1L;

    protected transient final List<T> repository = new CopyOnWriteArrayList<>();

    @Override
    public Optional<T> getById(final Long id) {
        return repository.stream().filter(
                entity -> entity.getId().equals(id)).findAny();
    }

    @Override
    public T save(final T entity) {
        if (entity.getId() == null || getById(entity.getId()).isEmpty()) {
            entity.setId(getAndIncreaseCurrentId());
            repository.add(entity);
        }
        else {
            final int index = repository.indexOf(getById(entity.getId()).get());
            repository.set(index, entity);
        }
        return entity;
    }

    @Override
    public void delete(final T entity) {
        if (!repository.contains(entity)) {
            throw new EntityNotFoundException(entity.getClass().getSimpleName());
        }
        else {
            repository.remove(entity);
        }
    }

    @Override
    public Iterable<T> getAll() {
        return repository;
    }

    private long getAndIncreaseCurrentId() {
        return currentId++;
    }
}
