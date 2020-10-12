package com.ra.course.linkedin.persistence.invitation;

import com.ra.course.linkedin.model.entity.notification.NotificationMethod;
import com.ra.course.linkedin.model.entity.other.ConnectionInvitation;
import com.ra.course.linkedin.model.entity.person.Account;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.persistence.configuration.RepositoryConfiguration;
import com.ra.course.linkedin.persistence.member.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.ra.course.linkedin.model.entity.other.ConnectionInvitationStatus.PENDING;
import static com.ra.course.linkedin.persistence.PersistenceIntegrationTestUtils.tryToDelete;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = RepositoryConfiguration.class)
public class InvitationRepositoryImplIntegrationTest {

    public static final long MEMBER_ID = 22L;
    public static final long SECOND_MEMBER_ID = 44L;

    @Autowired
    InvitationRepository invitationRepository;

    @Autowired
    MemberRepository memberRepository;

    private Member member;
    private Member secondMember;
    private ConnectionInvitation invitation;
    private ConnectionInvitation newConnection;

    @BeforeEach
    public void before() {
        member = createMember(MEMBER_ID);
        secondMember = createMember(SECOND_MEMBER_ID);
        invitation = createConnectionInvitation(member, secondMember);
        invitationRepository.save(invitation);
        memberRepository.save(member);
        memberRepository.save(secondMember);
    }

    @AfterEach
    public void after() {
        tryToDelete(memberRepository, member);
        tryToDelete(memberRepository, secondMember);
        tryToDelete(invitationRepository, invitation);
        tryToDelete(invitationRepository, newConnection);
    }

    @Test
    @DisplayName("When invitation not exists then should be added to repo")
    public void invitationRepositorySaveTestWhenInvitationNotExists() {
        //when
        invitationRepository.delete(invitation);
        ConnectionInvitation result = invitationRepository.save(invitation);
        //then
        assertNotNull(result.getId());
    }

    @Test
    @DisplayName("When invitation exists then should be returned from repo")
    public void invitationRepositorySaveTestWhenConnectionAuthorEqualsInvitationAuthor() {
        //given
        newConnection = createConnectionInvitation(member, secondMember);
        //when
        ConnectionInvitation result = invitationRepository.save(newConnection);
        //then
        assertEquals(result.getAuthor(), newConnection.getAuthor());
    }

    @Test
    @DisplayName("When invitation exists then should be returned from repo")
    public void invitationRepositorySaveTestWhenConnectionAuthorNotEqualsInvitationAuthor() {
        //given
        newConnection = createConnectionInvitation(member, secondMember);
        newConnection.setAuthor(secondMember);
        //when
        ConnectionInvitation result = invitationRepository.save(newConnection);
        //then
        assertEquals(result.getAuthor(), newConnection.getAuthor());
    }

    @Test
    @DisplayName("When invitation exists then should be returned from repo")
    public void invitationRepositorySaveTestWhenConnectionRecipientNotEqualsInvitationRecipient() {
        //given
        newConnection = createConnectionInvitation(member, secondMember);
        newConnection.setSentTo(member);
        //when
        ConnectionInvitation result = invitationRepository.save(newConnection);
        //then
        assertEquals(result.getSentTo(), newConnection.getSentTo());
    }

    @Test
    @DisplayName("When invitation exists then should be returned from repo")
    public void invitationRepositorySaveTestWhenConnectionAuthorContainsContact() {
        //given
        invitation.getAuthor().setContacts(List.of(secondMember));
        //when
        ConnectionInvitation result = invitationRepository.save(invitation);
        //then
        assertEquals(result.getId(), invitation.getId());
    }

    private Member createMember(long id) {

        return Member.builder()
                .id(id)
                .notificationMethods(NotificationMethod.values())
                .profile(Profile.builder()
                        .id(id)
                        .build())
                .account(Account.builder()
                        .build())
                .build();
    }

    private ConnectionInvitation createConnectionInvitation(Member author, Member recipient) {
        return ConnectionInvitation.builder()
                .author(author)
                .sentTo(recipient)
                .status(PENDING)
                .build();
    }
}
