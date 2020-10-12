package com.ra.course.linkedin.model.entity.person;

import com.ra.course.linkedin.model.entity.BaseEntity;
import com.ra.course.linkedin.model.entity.notification.NotificationMethod;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class Person extends BaseEntity {
    protected String name;
    protected Address address;
    protected String email;
    protected String phone;
    protected Account account;
    protected NotificationMethod[] notificationMethods;

    public Person(Long id, String name, Address address, String email,
                  String phone, Account account, NotificationMethod[] notificationMethods) {
        super(id);
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.account = account;
        this.notificationMethods = notificationMethods;
    }
}
