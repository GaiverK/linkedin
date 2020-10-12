package com.ra.course.linkedin.model.entity.notification;

import com.ra.course.linkedin.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public abstract class Notification extends BaseEntity {

    protected LocalDate createdOn;
    protected String content;

}
