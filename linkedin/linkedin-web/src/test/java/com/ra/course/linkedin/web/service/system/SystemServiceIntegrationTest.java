package com.ra.course.linkedin.web.service.system;

import com.ra.course.linkedin.model.entity.notification.NotificationMethod;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.member.MemberRepository;
import com.ra.course.linkedin.service.system.SystemService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.ra.course.linkedin.model.entity.notification.NotificationMethod.EMAIL;
import static com.ra.course.linkedin.model.entity.notification.NotificationMethod.PHONE;
import static com.ra.course.linkedin.service.notification.NotificationServiceImpl.LOCAL_DATE_FORMAT;
import static com.ra.course.linkedin.service.system.SystemServiceImpl.*;
import static com.ra.course.linkedin.web.service.ServiceIntegrationTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class SystemServiceIntegrationTest {
    @Autowired
    private SystemService systemService;
    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @AfterEach
    void tearDown() throws IOException {
        tryToDelete(memberRepository, member);
        Files.newBufferedWriter(Paths.get(EMAIL_PATH), StandardOpenOption.TRUNCATE_EXISTING);
        Files.newBufferedWriter(Paths.get(PUSH_PATH), StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Test
    @DisplayName("When member chose the email notification method and has new message, " +
            "then should be called NotificationService's sendEmailNotification method with NEW_MESSAGE arg")
    public void testSendMessageNotificationWhenMemberChoseEmailMethod() throws IOException {
        //given
        createAndSaveMemberWithEmailNotificationMethod();
        String expected = createInformation(NEW_MESSAGE, EMAIL_TYPE);
        //when
        systemService.sendMessageNotification(member);
        //then
        assertTrue(Files.lines(Paths.get(EMAIL_PATH)).anyMatch(line -> line
                .contains(expected)));
    }

    @Test
    @DisplayName("When member chose the phone notification method and has new message, " +
            "then should be called NotificationService's sendPushNotification method with NEW_MESSAGE arg")
    public void testSendMessageNotificationWhenMemberChosePhoneMethod() throws IOException {
        //given
        createAndSaveMemberWithPhoneNotificationMethod();
        String expected = createInformation(NEW_MESSAGE, PHONE_TYPE);
        //when
        systemService.sendMessageNotification(member);
        //then
        assertTrue(Files.lines(Paths.get(PUSH_PATH)).anyMatch(line -> line
                .contains(expected)));
    }

    @Test
    @DisplayName("When member chose the email notification method and has new connection pending, " +
            "then should be called NotificationService's sendEmailNotification method with NEW_CONN_PENDING arg")
    public void testSendConnPendingNotificationWhenMemberChoseEmailMethod() throws IOException {
        //given
        createAndSaveMemberWithEmailNotificationMethod();
        String expected = createInformation(NEW_CONN_PENDING, EMAIL_TYPE);
        //when
        systemService.sendConnPendingNotification(member);
        //then
        assertTrue(Files.lines(Paths.get(EMAIL_PATH)).anyMatch(line -> line
                .contains(expected)));
    }

    @Test
    @DisplayName("When member chose the phone notification method and has new connection pending, " +
            "then should be called NotificationService's sendPushNotification method with NEW_CONN_PENDING arg")
    public void testSendConnPendingNotificationWhenMemberChosePhoneMethod() throws IOException {
        //given
        createAndSaveMemberWithPhoneNotificationMethod();
        String expected = createInformation(NEW_CONN_PENDING, PHONE_TYPE);
        //when
        systemService.sendConnPendingNotification(member);
        //then
        assertTrue(Files.lines(Paths.get(PUSH_PATH)).anyMatch(line -> line
                .contains(expected)));
    }

    @Test
    @DisplayName("When member chose the email notification method and has new suggestion, " +
            "then should be called NotificationService's sendEmailNotification method with NEW_SUGGESTION arg")
    public void testSendNewSuggestNotificationWhenMemberChoseEmailMethod() throws IOException {
        //given
        createAndSaveMemberWithEmailNotificationMethod();
        String expected = createInformation(NEW_SUGGESTION, EMAIL_TYPE);
        //when
        systemService.sendNewSuggestNotification(member);
        //then
        assertTrue(Files.lines(Paths.get(EMAIL_PATH)).anyMatch(line -> line
                .contains(expected)));
    }

    @Test
    @DisplayName("When member chose the phone notification method and has new suggestion, " +
            "then should be called NotificationService's sendPushNotification method with NEW_SUGGESTION arg")
    public void testSendNewSuggestionNotificationWhenMemberChosePhoneMethod() throws IOException {
        //given
        createAndSaveMemberWithPhoneNotificationMethod();
        String expected = createInformation(NEW_SUGGESTION, PHONE_TYPE);
        //when
        systemService.sendNewSuggestNotification(member);
        //then
        assertTrue(Files.lines(Paths.get(PUSH_PATH)).anyMatch(line -> line
                .contains(expected)));
    }

    @Test
    @DisplayName("When member chose the email notification method and " +
            "has new recommendation, then should be called NotificationService's " +
            "sendEmailNotification method with NEW_RECOMMENDAT arg")
    public void testSendRecommendationNotificationWhenMemberChoseEmailMethod() throws IOException {
        createAndSaveMemberWithEmailNotificationMethod();
        String expected = createInformation(NEW_RECOMMENDAT, EMAIL_TYPE);
        //when
        systemService.sendRecommendationNotification(member);
        //then
        assertTrue(Files.lines(Paths.get(EMAIL_PATH)).anyMatch(line -> line
                .contains(expected)));
    }

    @Test
    @DisplayName("When member chose the the phone notification method and " +
            "has new recommendation, then should be called NotificationService's " +
            "sendPushNotification method with NEW_RECOMMENDAT arg")
    public void testSendRecommendationNotificationWhenMemberChosePhoneMethod() throws IOException {
        //given
        createAndSaveMemberWithPhoneNotificationMethod();
        String expected = createInformation(NEW_RECOMMENDAT, PHONE_TYPE);
        //when
        systemService.sendRecommendationNotification(member);
        //then
        assertTrue(Files.lines(Paths.get(PUSH_PATH)).anyMatch(line -> line
                .contains(expected)));
    }

    public void createAndSaveMemberWithEmailNotificationMethod() {
        member = Member.builder()
                .name("MemberWithEmailNotifications")
                .notificationMethods(new NotificationMethod[]{EMAIL})
                .build();
        memberRepository.save(member);
    }

    public void createAndSaveMemberWithPhoneNotificationMethod() {
        member = Member.builder()
                .name("MemberWithPhoneNotifications")
                .notificationMethods(new NotificationMethod[]{PHONE})
                .build();
        memberRepository.save(member);
    }

    private String createInformation(String content, String type) {
        final DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT);

        return String.format("%s %s to %s (id = %d): %s",
                LocalDateTime.now().format(formatter),
                type,
                member.getName(),
                member.getId(),
                content);
    }
}
