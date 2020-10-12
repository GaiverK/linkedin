package com.ra.course.linkedin.web.service;

import com.ra.course.linkedin.model.entity.BaseEntity;
import com.ra.course.linkedin.persistence.BaseRepository;

import java.util.logging.Logger;

public class ServiceIntegrationTestUtils {
    public static final String EMAIL_TYPE = "email";
    public static final String PHONE_TYPE = "message";

    public static final Logger LOGGER =
            Logger.getLogger(ServiceIntegrationTestUtils.class.getName());

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
