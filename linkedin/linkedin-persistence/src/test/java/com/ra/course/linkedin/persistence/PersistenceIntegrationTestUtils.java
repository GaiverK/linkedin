package com.ra.course.linkedin.persistence;

import com.ra.course.linkedin.model.entity.BaseEntity;

import java.util.logging.Logger;

public class PersistenceIntegrationTestUtils {

    public static final Logger LOGGER =
            Logger.getLogger(PersistenceIntegrationTestUtils.class.getName());

    @SuppressWarnings(value = "unchecked, rawtypes")
    public static void tryToDelete(BaseRepository repository, BaseEntity entity) {
        try {
            repository.delete(entity);
        } catch (Exception e) {
            LOGGER.info(String.format("%s. But here it is ok.",
                    e.getClass().getSimpleName()));
        }
    }
}
