package com.ra.course.linkedin.persistence.utils;

import com.ra.course.linkedin.model.entity.BaseEntity;
import com.ra.course.linkedin.persistence.BaseRepository;

import java.util.stream.StreamSupport;

public class RepositoryTestUtil {
    public static long getSizeOfRepository(BaseRepository<? extends BaseEntity> repository) {
        return StreamSupport.stream(repository.getAll().spliterator(), false).count();
    }
}
