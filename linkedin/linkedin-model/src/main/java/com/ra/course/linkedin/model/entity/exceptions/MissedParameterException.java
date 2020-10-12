package com.ra.course.linkedin.model.entity.exceptions;

public class MissedParameterException extends RuntimeException {
    public static final long serialVersionUID = 1;

    public MissedParameterException(String parameterName, String entityName) {
        super(parameterName.concat(" not found at ").concat(entityName));
    }
}
