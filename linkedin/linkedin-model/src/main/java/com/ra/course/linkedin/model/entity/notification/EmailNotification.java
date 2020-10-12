package com.ra.course.linkedin.model.entity.notification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class EmailNotification extends Notification {

    private String email;

    public EmailNotification(LocalDate createdOn, String content,
                             String email) {
        super(createdOn, content);
        this.email = email;
    }

}
