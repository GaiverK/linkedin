package com.ra.course.linkedin.service.notification;

import com.ra.course.linkedin.model.entity.person.Member;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger LOGGER = Logger.getLogger(NotificationServiceImpl.class.getName());
    public static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void sendEmailNotification(final Member member, final String content, final String path) {
        final DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT);
        final String information = String.format("%s email to %s (id = %d): %s\n",
                LocalDateTime.now().format(formatter),
                member.getName(),
                member.getId(),
                content);
        writeMessage(path, information);
    }

    @Override
    public void sendPushNotification(final Member member, final String content, final String path) {
        final DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT);
        final String information = String.format("%s message to %s (id = %d): %s\n",
                LocalDateTime.now().format(formatter),
                member.getName(),
                member.getId(),
                content);
        writeMessage(path, information);
    }

    private void writeMessage(final String path, final String information) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path),
                StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            writer.write(information);
        } catch (IOException e) {
            LOGGER.severe("IOException for Notification");
        }
    }
}
