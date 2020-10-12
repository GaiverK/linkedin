package com.ra.course.linkedin.service.invitation;

import com.ra.course.linkedin.model.entity.notification.NotificationMethod;
import com.ra.course.linkedin.model.entity.other.ConnectionInvitation;
import com.ra.course.linkedin.model.entity.person.Account;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.persistence.invitation.InvitationRepository;
import com.ra.course.linkedin.persistence.member.MemberRepository;
import com.ra.course.linkedin.service.member.MemberService;
import com.ra.course.linkedin.service.system.SystemService;
import com.ra.course.linkedin.service.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;

import static com.ra.course.linkedin.model.entity.other.ConnectionInvitationStatus.*;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class InvitationServiceImplMockTest {
    public static final long MEMBER_ID = 22L;
    public static final long SECOND_MEMBER_ID = 44L;
    public static final String MEMBER_MAIL_RU = "member@mail.ru";
    public static final String PASSWORD = "12345";
    private InvitationService invitationService;
    private final InvitationRepository mockInvitationRepository =
            mock(InvitationRepository.class);
    private final MemberRepository mockMemberRepository = mock(MemberRepository.class);
    private final Utils mockUtils = mock(Utils.class);
    private final MemberService mockMemberService = mock(MemberService.class);
    private final SystemService mockSystemService = mock(SystemService.class);
    private Member member;
    private Member secondMember;

    @BeforeEach
    public void before() {
        member = createMember(MEMBER_ID);
        secondMember = createMember(SECOND_MEMBER_ID);

        invitationService = new InvitationServiceImpl(
                mockInvitationRepository,
                mockMemberRepository,
                mockUtils,
                mockMemberService,
                mockSystemService);
    }

    @Test
    @DisplayName("When system creates connection invitation, then this invitation should be added to InvitationRepository")
    public void testCreateConnectionInvitation() {
        //when
        when(mockMemberService.getMemberById(member.getId())).thenReturn(ofNullable(member));
        when(mockMemberService.getMemberById(secondMember.getId())).thenReturn(ofNullable(secondMember));
        invitationService.createConnectionInvitation(member.getId(), secondMember.getId());
        //then
        verify(mockInvitationRepository).save(any(ConnectionInvitation.class));
    }

    @Test
    @DisplayName("When member send connection invitation, then this invitation should be added to: " +
            "\n 1. InvitationRepository" +
            "\n 2. Author's list of sent connection invitations" +
            "\n 3. Recipient's list of incoming connection invitations" +
            "\n and should be send notification by SystemService")
    public void testSendConnectionRequest() {
        //given
        ConnectionInvitation invitation = createInvitation();
        //when
        invitationService.sendConnectionRequest(invitation);
        invitationService.sendConnectionRequest(invitation);
        //then
        assertTrue(invitation.getAuthor().getSentConnectionInvitations()
                .contains(invitation));
        verify(mockMemberRepository).save(invitation.getAuthor());
        assertTrue(invitation.getSentTo().getIncomingConnectionInvitations()
                .contains(invitation));
        verify(mockMemberRepository).save(invitation.getSentTo());
        verify(mockSystemService).sendConnPendingNotification(invitation.getSentTo());
    }

    @Test
    @DisplayName("When member accept the connection request, " +
            "then status of invitation should be changed to ACCEPTED, " +
            "connection participants should be added to each other in contacts list and " +
            "notification of new suggestion must be send to connection participants.")
    public void testAcceptConnectionRequest() {
        //given
        ConnectionInvitation invitation = createInvitation();
        Member author = invitation.getAuthor();
        Member recipient = invitation.getSentTo();
        //when
        when(mockUtils.findInvitation(invitation)).thenReturn(invitation);
        invitationService.acceptConnectionRequest(invitation);
        //then
        assertEquals(invitation.getStatus(), ACCEPTED);
        verify(mockInvitationRepository).save(invitation);
        verify(mockMemberService).addContact(author, recipient);
        verify(mockMemberService).addContact(recipient, author);
        verify(mockSystemService).sendNewSuggestNotification(author);
        verify(mockSystemService).sendNewSuggestNotification(recipient);
    }

    @Test
    @DisplayName("When the connection request is rejected, " +
            "then status of connection invitation should be changed to REJECTED")
    public void testRejectConnectionRequest() {
        //given
        ConnectionInvitation invitation = createInvitation();
        //when
        when(mockUtils.findInvitation(invitation)).thenReturn(invitation);
        invitationService.rejectConnectionRequest(invitation);
        //then
        assertEquals(invitation.getStatus(), REJECTED);
        verify(mockInvitationRepository).save(invitation);
    }

    private Member createMember(long id) {

        return Member.builder()
                .id(id)
                .notificationMethods(NotificationMethod.values())
                .profile(Profile.builder()
                        .id(id)
                        .build())
                .email(MEMBER_MAIL_RU)
                .account(Account.builder()
                        .password(PASSWORD)
                        .build())
                .build();
    }

    private ConnectionInvitation createInvitation() {
        Member sender = new Member();
        Member recipient = new Member();
        return ConnectionInvitation.builder()
                .author(sender)
                .sentTo(recipient)
                .status(PENDING)
                .build();
    }
}
