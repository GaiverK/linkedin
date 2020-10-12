package com.ra.course.linkedin.service.system;

import com.ra.course.linkedin.model.entity.exceptions.MissedParameterException;
import com.ra.course.linkedin.model.entity.notification.Notification;
import com.ra.course.linkedin.model.entity.notification.NotificationMethod;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class SystemServiceImpl implements SystemService {

    public static final String NEW_MESSAGE = "You have new message in LinkedIn";
    public static final String NEW_CONN_PENDING = "You have new connection pending in LinkedIn";
    public static final String NEW_SUGGESTION = "You have new suggestion in LinkedIn";
    public static final String NEW_RECOMMENDAT = "You have new recommendation in LinkedIn";
    public static final String EMAIL_PATH = "src/test/resources/notification/.email-notifications.txt";
    public static final String PUSH_PATH = "src/test/resources/notification/.push-notifications.txt";

    private final NotificationService notificatService;

    @Override
    public void sendMessageNotification(final Member member) {
        if (member.getNotificationMethods() != null) {
            final List<NotificationMethod> methods = Arrays.asList(member.getNotificationMethods());
            if (methods.contains(NotificationMethod.EMAIL)) {
                notificatService.sendEmailNotification(member, NEW_MESSAGE, EMAIL_PATH);
            }
            if (methods.contains(NotificationMethod.PHONE)) {
                notificatService.sendPushNotification(member, NEW_MESSAGE, PUSH_PATH);
            }
        }
        else {
            throw new MissedParameterException(Notification.class.getSimpleName(), Member.class.getSimpleName());
        }
    }

    @Override
    public void sendConnPendingNotification(final Member member) {
        if (member.getNotificationMethods() != null) {
            final List<NotificationMethod> methods = Arrays.asList(member.getNotificationMethods());
            if (methods.contains(NotificationMethod.EMAIL)) {
                notificatService.sendEmailNotification(member, NEW_CONN_PENDING, EMAIL_PATH);
            }
            if (methods.contains(NotificationMethod.PHONE)) {
                notificatService.sendPushNotification(member, NEW_CONN_PENDING, PUSH_PATH);
            }
        }
        else {
            throw new MissedParameterException(Notification.class.getSimpleName(), Member.class.getSimpleName());
        }
    }

    @Override
    public void sendNewSuggestNotification(final Member member) {
        if (member.getNotificationMethods() != null) {
            final List<NotificationMethod> methods = Arrays.asList(member.getNotificationMethods());
            if (methods.contains(NotificationMethod.EMAIL)) {
                notificatService.sendEmailNotification(member, NEW_SUGGESTION, EMAIL_PATH);
            }
            if (methods.contains(NotificationMethod.PHONE)) {
                notificatService.sendPushNotification(member, NEW_SUGGESTION, PUSH_PATH);
            }
        }
        else {
            throw new MissedParameterException(Notification.class.getSimpleName(), Member.class.getSimpleName());
        }
    }

    @Override
    public void sendRecommendationNotification(final Member member) {
        if (member.getNotificationMethods() != null) {
            final List<NotificationMethod> methods = Arrays.asList(member.getNotificationMethods());
            if (methods.contains(NotificationMethod.EMAIL)) {
                notificatService.sendEmailNotification(member, NEW_RECOMMENDAT, EMAIL_PATH);
            }
            if (methods.contains(NotificationMethod.PHONE)) {
                notificatService.sendPushNotification(member, NEW_RECOMMENDAT, PUSH_PATH);
            }
        }
        else {
            throw new MissedParameterException(Notification.class.getSimpleName(), Member.class.getSimpleName());
        }
    }
}
