package com.ra.course.linkedin.service.system;

import com.ra.course.linkedin.model.entity.exceptions.MissedParameterException;
import com.ra.course.linkedin.model.entity.notification.Notification;
import com.ra.course.linkedin.model.entity.notification.NotificationMethod;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static com.ra.course.linkedin.model.entity.notification.NotificationMethod.EMAIL;
import static com.ra.course.linkedin.model.entity.notification.NotificationMethod.PHONE;
import static com.ra.course.linkedin.service.system.SystemServiceImpl.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SystemServiceImplMockTest {

    private SystemService systemService;
    private final NotificationService mockNotificationService =
            mock(NotificationService.class);

    @BeforeEach
    public void before() {
        systemService = new SystemServiceImpl(mockNotificationService);
    }

    @Test
    @DisplayName("When member chose the email notification method and has new message, " +
            "then should be called NotificationService's sendEmailNotification method with NEW_MESSAGE arg")
    public void testSendMessageNotificationWhenMemberChoseEmailMethod() {
        //given
        Member member = createMemberWithEmailNotificationMethod();
        //when
        systemService.sendMessageNotification(member);
        //then
        verify(mockNotificationService).sendEmailNotification(member, NEW_MESSAGE, EMAIL_PATH);
    }

    @Test
    @DisplayName("When member chose the phone notification method and has new message, " +
            "then should be called NotificationService's sendPushNotification method with NEW_MESSAGE arg")
    public void testSendMessageNotificationWhenMemberChosePhoneMethod() {
        //given
        Member member = createMemberWithPhoneNotificationMethod();
        //when
        systemService.sendMessageNotification(member);
        //then
        verify(mockNotificationService).sendPushNotification(member, NEW_MESSAGE, PUSH_PATH);
    }

    @Test
    @DisplayName("When member chose the email notification method and has new connection pending, " +
            "then should be called NotificationService's sendEmailNotification method with NEW_CONN_PENDING arg")
    public void testSendConnPendingNotificationWhenMemberChoseEmailMethod() {
        //given
        Member member = createMemberWithEmailNotificationMethod();
        //when
        systemService.sendConnPendingNotification(member);
        //then
        verify(mockNotificationService).sendEmailNotification(member, NEW_CONN_PENDING, EMAIL_PATH);
    }

    @Test
    @DisplayName("When member chose the phone notification method and has new connection pending, " +
            "then should be called NotificationService's sendPushNotification method with NEW_CONN_PENDING arg")
    public void testSendConnPendingNotificationWhenMemberChosePhoneMethod() {
        //given
        Member member = createMemberWithPhoneNotificationMethod();
        //when
        systemService.sendConnPendingNotification(member);
        //then
        verify(mockNotificationService).sendPushNotification(member, NEW_CONN_PENDING, PUSH_PATH);
    }

    @Test
    @DisplayName("When member chose the email notification method and has new suggestion, " +
            "then should be called NotificationService's sendEmailNotification method with NEW_SUGGESTION arg")
    public void testSendNewSuggestNotificationWhenMemberChoseEmailMethod() {
        //given
        Member member = createMemberWithEmailNotificationMethod();
        //when
        systemService.sendNewSuggestNotification(member);
        //then
        verify(mockNotificationService).sendEmailNotification(member, NEW_SUGGESTION, EMAIL_PATH);
    }

    @Test
    @DisplayName("When member chose the phone notification method and has new suggestion, " +
            "then should be called NotificationService's sendPushNotification method with NEW_SUGGESTION arg")
    public void testSendNewSuggestionNotificationWhenMemberChosePhoneMethod() {
        //given
        Member member = createMemberWithPhoneNotificationMethod();
        //when
        systemService.sendNewSuggestNotification(member);
        //then
        verify(mockNotificationService).sendPushNotification(member, NEW_SUGGESTION, PUSH_PATH);
    }

    @Test
    @DisplayName("When member chose the email notification method and " +
            "has new recommendation, then should be called NotificationService's " +
            "sendEmailNotification method with NEW_RECOMMENDAT arg")
    public void testSendRecommendationNotificationWhenMemberChoseEmailMethod() {
        //given
        Member member = createMemberWithEmailNotificationMethod();
        //when
        systemService.sendRecommendationNotification(member);
        //then
        verify(mockNotificationService).sendEmailNotification(member,
                NEW_RECOMMENDAT, EMAIL_PATH);
    }

    @Test
    @DisplayName("When member chose the the phone notification method and " +
            "has new recommendation, then should be called NotificationService's " +
            "sendPushNotification method with NEW_RECOMMENDAT arg")
    public void testSendRecommendationNotificationWhenMemberChosePhoneMethod() {
        //given
        Member member = createMemberWithPhoneNotificationMethod();
        //when
        systemService.sendRecommendationNotification(member);
        //then
        verify(mockNotificationService).sendPushNotification(member,
                NEW_RECOMMENDAT, PUSH_PATH);
    }

    @Test
    @DisplayName("When member doesn't have any notification methods, then throws MissedParameterException")
    public void testSendNotificationWhenMemberHaveNotNotificationMethod() {
        //given
        Member member = new Member();
        String exceptionMessage = Notification.class.getSimpleName();
        //when
        Executable suggestExecutable = () -> systemService.sendNewSuggestNotification(member);
        Executable recommendationExecutable = () -> systemService.sendRecommendationNotification(member);
        Executable messageExecutable = () -> systemService.sendMessageNotification(member);
        Executable connectionExecutable = () -> systemService.sendConnPendingNotification(member);
        //then
        Throwable suggestException = assertThrows(MissedParameterException.class, suggestExecutable);
        Throwable recommendationException = assertThrows(MissedParameterException.class, recommendationExecutable);
        Throwable messageException = assertThrows(MissedParameterException.class, messageExecutable);
        Throwable connectionException = assertThrows(MissedParameterException.class, connectionExecutable);

        assertAll(
                () -> assertTrue(suggestException.getMessage().contains(exceptionMessage)),
                () -> assertTrue(recommendationException.getMessage().contains(exceptionMessage)),
                () -> assertTrue(messageException.getMessage().contains(exceptionMessage)),
                () -> assertTrue(connectionException.getMessage().contains(exceptionMessage))
        );

    }

    private Member createMemberWithEmailNotificationMethod() {
        Member member = new Member();
        member.setNotificationMethods(new NotificationMethod[]{EMAIL});
        return member;
    }

    private Member createMemberWithPhoneNotificationMethod() {
        Member member = new Member();
        member.setNotificationMethods(new NotificationMethod[]{PHONE});
        return member;
    }
}
