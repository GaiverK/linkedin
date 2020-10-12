package com.ra.course.linkedin.model.entity.person;

import com.ra.course.linkedin.model.entity.notification.NotificationMethod;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Admin extends Person {
    public Admin(String name, Address address, String email, String phone,
                 Account account, NotificationMethod[] notificationMethods) {
        super(name, address, email, phone, account, notificationMethods);
    }
}
