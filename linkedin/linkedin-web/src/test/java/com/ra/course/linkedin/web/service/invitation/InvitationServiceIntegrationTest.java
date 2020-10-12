package com.ra.course.linkedin.web.service.invitation;

import com.ra.course.linkedin.model.entity.notification.NotificationMethod;
import com.ra.course.linkedin.model.entity.other.ConnectionInvitation;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.invitation.InvitationRepository;
import com.ra.course.linkedin.persistence.member.MemberRepository;
import com.ra.course.linkedin.service.invitation.InvitationService;
import com.ra.course.linkedin.service.notification.NotificationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static com.ra.course.linkedin.model.entity.other.ConnectionInvitationStatus.*;
import static com.ra.course.linkedin.service.system.SystemServiceImpl.EMAIL_PATH;
import static com.ra.course.linkedin.service.system.SystemServiceImpl.PUSH_PATH;
import static com.ra.course.linkedin.web.service.ServiceIntegrationTestUtils.tryToDelete;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InvitationServiceIntegrationTest {

    @Autowired
    private InvitationService invitationService;
    @Autowired
    private InvitationRepository invitationRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private NotificationService notificationService;

    private ConnectionInvitation invitation;
    private Member sender;
    private Member recipient;

    @BeforeEach
    void setUp() {
        sender = createSender();
        recipient = createRecipient();
        invitation = createInvitation();
        invitationRepository.save(invitation);
        memberRepository.save(sender);
        memberRepository.save(recipient);
    }

    @AfterEach
    void tearDown() throws IOException {
        tryToDelete(invitationRepository, invitation);
        tryToDelete(memberRepository, sender);
        tryToDelete(memberRepository, recipient);
        Files.newBufferedWriter(Paths.get(EMAIL_PATH), StandardOpenOption.TRUNCATE_EXISTING);
        Files.newBufferedWriter(Paths.get(PUSH_PATH), StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Test
    @DisplayName("When member send connection invitation, " +
            "then this invitation should be added to: " +
            "\n 1. InvitationRepository" +
            "\n 2. Author's list of sent connection invitations" +
            "\n 3. Recipient's list of incoming connection invitations")
    public void testSendConnectionRequest() {
        //when
        invitationService.sendConnectionRequest(invitation);
        //then
        final ConnectionInvitation invitationFromRepo = invitationRepository
                .getById(invitation.getId()).get();
        assertAll(
                () -> assertEquals(invitationFromRepo, invitation),
                () -> assertTrue(invitationFromRepo.getAuthor()
                        .getSentConnectionInvitations().contains(invitation)),
                () -> assertTrue(invitationFromRepo.getSentTo()
                        .getIncomingConnectionInvitations().contains(invitation)),
                () -> assertEquals(memberRepository.getById(invitationFromRepo
                        .getAuthor().getId()).get().getSentConnectionInvitations().get(0),
                        invitation),
                () -> assertEquals(memberRepository.getById(invitationFromRepo.getSentTo()
                        .getId()).get().getIncomingConnectionInvitations().get(0),
                        invitation)
        );
    }

    @Test
    @DisplayName("When member accept the connection request, " +
            "then status of invitation should be changed to ACCEPTED, " +
            "connection participants should be added to each other in contacts list ")
    public void testAcceptConnectionRequest() {
        //when
        invitationService.acceptConnectionRequest(invitation);
        //then
        final ConnectionInvitation invitationFromRepo = invitationRepository
                .getById(invitation.getId()).get();
        final Member recipientFromRepo = memberRepository.getById(
                invitation.getSentTo().getId()).get();
        final Member authorFromRepo = memberRepository.getById(
                invitationFromRepo.getAuthor().getId()).get();
        assertAll(
                () -> assertEquals(invitationFromRepo, invitation),
                () -> assertEquals(invitationFromRepo.getStatus(), ACCEPTED),
                () -> assertEquals(authorFromRepo.getContacts().get(0), recipientFromRepo),
                () -> assertEquals(recipientFromRepo.getContacts().get(0), authorFromRepo)
        );
    }

    @Test
    @DisplayName("When the connection request is rejected, " +
            "then status of connection invitation should be changed to REJECTED")
    public void testRejectConnectionRequest() {
        //when
        invitationService.rejectConnectionRequest(invitation);
        final ConnectionInvitation invitationFromRepo = invitationRepository.getById(
                invitation.getId()).get();
        //then
        assertAll(
                () -> assertEquals(invitationFromRepo, invitation),
                () -> assertEquals(invitationFromRepo.getStatus(), REJECTED)
        );
    }

    private ConnectionInvitation createInvitation() {
        return ConnectionInvitation.builder()
                .author(sender)
                .sentTo(recipient)
                .status(PENDING)
                .build();
    }

    private Member createSender() {
        return Member.builder()
                .name("Adam")
                .notificationMethods(NotificationMethod.values())
                .build();
    }

    private Member createRecipient() {
        return Member.builder()
                .name("Eve")
                .notificationMethods(NotificationMethod.values())
                .build();
    }
}
