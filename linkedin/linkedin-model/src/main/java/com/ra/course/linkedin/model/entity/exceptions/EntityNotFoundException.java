package com.ra.course.linkedin.model.entity.exceptions;

public class EntityNotFoundException extends RuntimeException {

    public static final long serialVersionUID = 1;

    public EntityNotFoundException(String entityName) {
        super(entityName.concat(" not found."));
    }
}
