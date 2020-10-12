package com.ra.course.linkedin.service.notification;

import com.ra.course.linkedin.model.entity.person.Member;

public interface NotificationService {
    void sendEmailNotification(Member member, String content, String path);
    void sendPushNotification(Member member, String content, String path);
}
