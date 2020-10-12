package com.ra.course.linkedin.service.notification;

import com.ra.course.linkedin.model.entity.person.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NotificationServiceImplIntegrationTest {

    private static final String CONTENT = "content";
    private static final String EMAIL_PATH = "src/test/resources/notification/.email-notifications.txt";
    private static final String PUSH_PATH = "src/test/resources/notification/.push-notifications.txt";
    private static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String EMAIL_TYPE = "email";
    private static final String PHONE_TYPE = "message";

    private final NotificationService notificationService = new NotificationServiceImpl();

    private Member member;

    @AfterEach
    void tearDown() throws IOException {
        Files.newBufferedWriter(Paths.get(EMAIL_PATH), StandardOpenOption.TRUNCATE_EXISTING);
        Files.newBufferedWriter(Paths.get(PUSH_PATH), StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Test
    @DisplayName("When sends the email notification," +
            "then should be added current information to file with all email notifications")
    public void testSendEmailNotification() throws IOException {
        //given
        member = createMember();
        String expected = createInformation(CONTENT, EMAIL_TYPE);
        //when
        notificationService.sendEmailNotification(member, CONTENT, EMAIL_PATH);
        //then
        assertTrue(Files.lines(Paths.get(EMAIL_PATH)).anyMatch(line -> line.contains(expected)));
    }

    @Test
    @DisplayName("When sends the push notification," +
            "then should be added current information to file with all push notifications")
    public void testSendPushNotification() throws IOException {
        //given
        member = createMember();
        String expected = createInformation(CONTENT, PHONE_TYPE);
        //when
       notificationService.sendPushNotification(member, CONTENT, PUSH_PATH);
        //then
        assertTrue(Files.lines(Paths.get(PUSH_PATH)).anyMatch(line -> line.contains(expected)));
    }

    private Member createMember() {
        return Member.builder().name("name").id(1L).build();
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
