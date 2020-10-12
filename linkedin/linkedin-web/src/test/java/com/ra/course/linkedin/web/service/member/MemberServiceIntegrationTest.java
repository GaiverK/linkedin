package com.ra.course.linkedin.web.service.member;

import com.ra.course.linkedin.model.entity.notification.NotificationMethod;
import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.model.entity.other.Message;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.company.CompanyRepository;
import com.ra.course.linkedin.persistence.member.MemberRepository;
import com.ra.course.linkedin.service.member.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.ra.course.linkedin.web.service.ServiceIntegrationTestUtils.tryToDelete;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MemberServiceIntegrationTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CompanyRepository companyRepository;

    private Member mainMember;
    private Member additionalMember;
    private Company company;
    private List<Member> recipients;

    @BeforeEach
    void setUp() {
        mainMember = createMember();
        additionalMember = createMember();
        company = createCompany();
        memberRepository.save(mainMember);
        memberRepository.save(additionalMember);
        companyRepository.save(company);
        recipients = createRecipients();
        recipients.forEach(member -> memberRepository.save(member));
    }

    @AfterEach
    void tearDown() {
        tryToDelete(memberRepository, mainMember);
        tryToDelete(memberRepository, additionalMember);
        tryToDelete(companyRepository, company);
        recipients.forEach(member -> tryToDelete(memberRepository, member));
    }

    @Test
    @DisplayName("When member follow company, " +
            "then company should be added to members's list of following companies " +
            "and member should be added to company's list of followers." +
            "When member unsubscribe from the company, " +
            "then company should be removed from member's list of following companies" +
            " and member should be removed from company's list of followers.")
    public void testWhenMemberFollowsAndUnFollowsCompany() {
        //when
        memberService.followCompany(mainMember, company);
        //then
        final Member memberFromRepo = memberRepository.getById(mainMember.getId()).get();
        final Company companyFromRepo = companyRepository.getById(memberFromRepo
                .getFollowingCompanies().get(0).getId()).get();
        assertAll(
                () -> assertEquals(memberFromRepo, mainMember),
                () -> assertEquals(companyFromRepo, company),
                () -> assertEquals(companyFromRepo.getFollowers().get(0), memberFromRepo),
                () -> assertEquals(memberFromRepo.getFollowingCompanies().get(0),
                        companyFromRepo)
        );
        //when
        memberService.unFollowCompany(mainMember, company);
        //then
        assertAll(
                () -> assertEquals(memberFromRepo, mainMember),
                () -> assertEquals(companyFromRepo, company),
                () -> assertEquals(memberFromRepo.getFollowingCompanies().size(), 0),
                () -> assertEquals(companyFromRepo.getFollowers().size(), 0)
        );
    }

    @Test
    @DisplayName("When member follow followed member, " +
            "then followed will be added to following list, " +
            "then member will be added to followers list." +
            "When member unsubscribe from the followed member, " +
            "then followed will be removed from following list, " +
            "then member will be removed from followers list")
    void testWhenMemberFollowsMemberAndUnFollowsMember() {
        //when
        memberService.followMember(mainMember, additionalMember);
        //then
        final Member mainMemberFromRepo = memberRepository.getById(
                mainMember.getId()).get();
        final Member additionalMemberFromRepo = memberRepository.getById(
                additionalMember.getId()).get();
        assertAll(
                () -> assertEquals(mainMemberFromRepo.getFollowingMembers().get(0),
                        additionalMemberFromRepo),
                () -> assertEquals(additionalMemberFromRepo.getFollowers().get(0),
                        mainMemberFromRepo),
                () -> assertEquals(mainMemberFromRepo
                        .getFollowingMembers().get(0), additionalMemberFromRepo),
                () -> assertEquals(additionalMemberFromRepo
                        .getFollowers().get(0), mainMemberFromRepo)
        );
        //when
        memberService.unFollowMember(mainMember, additionalMember);
        //then
        assertAll(
                () -> assertEquals(mainMemberFromRepo, mainMember),
                () -> assertEquals(additionalMemberFromRepo, additionalMember),
                () -> assertEquals(mainMemberFromRepo.getFollowingMembers().size(), 0),
                () -> assertEquals(additionalMemberFromRepo.getFollowers().size(), 0)
        );
    }

    @Test
    @DisplayName("When contact was made, then member should be added " +
            "to another member's list of contacts")
    public void testAddContact() {
        //when
        memberService.addContact(mainMember, additionalMember);
        //then
        final Member memberFromRepo = memberRepository.getById(
                mainMember.getId()).get();
        assertAll(
                () -> assertEquals(memberFromRepo.getContacts().size(), 1),
                () -> assertTrue(memberFromRepo.getContacts().contains(additionalMember))
        );
    }

    @Test
    @DisplayName("When member send message, then it must de added " +
            "to his sent message list and " +
            "to recipients incoming messages lists")
    void testWhenMemberSentMessage() {
        //given
        Message message = createMessage();
        recipients = message.getSendTo();
        //when
        memberService.sendMessage(message);
        //then
        assertAll(
                () -> assertEquals(memberRepository.getById(mainMember.getId()).get()
                        .getSentMessages().get(0), message),
                () -> recipients.forEach(recipient ->
                        assertEquals(memberRepository.getById(recipient.getId()).get()
                                .getIncomingMessages().get(0), message)),
                () -> assertEquals(memberRepository.getById(recipients.get(0).getId()).get(),
                        recipients.get(0)),
                () -> assertEquals(memberRepository.getById(recipients.get(1).getId()).get(),
                        recipients.get(1))
        );
    }

    private Member createMember() {
        return Member.builder().build();
    }

    private Company createCompany() {
        return Company.builder()
                .followers(new LinkedList<>())
                .postedJobs(new LinkedList<>())
                .build();
    }

    private Message createMessage() {
        return Message.builder()
                .author(mainMember)
                .sendTo(recipients)
                .build();
    }

    private List<Member> createRecipients() {
        recipients = new ArrayList<>();
        Member recipient0 = Member.builder()
                .notificationMethods(NotificationMethod.values())
                .build();
        recipients.add(recipient0);
        Member recipient1 = Member.builder()
                .notificationMethods(NotificationMethod.values())
                .build();
        recipients.add(recipient1);
        return recipients;
    }
}
