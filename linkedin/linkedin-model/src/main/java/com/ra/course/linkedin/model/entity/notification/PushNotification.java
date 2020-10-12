package com.ra.course.linkedin.model.entity.notification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class PushNotification extends Notification {

    private String phoneNumber;

    public PushNotification(LocalDate createdOn, String content,
                            String phoneNumber) {
        super(createdOn, content);
        this.phoneNumber = phoneNumber;
    }

}
