package com.ra.course.linkedin.service.member;

import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.model.entity.other.Message;
import com.ra.course.linkedin.model.entity.person.Account;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.company.CompanyRepository;
import com.ra.course.linkedin.persistence.member.MemberRepository;
import com.ra.course.linkedin.service.system.SystemService;
import com.ra.course.linkedin.service.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceImplMockTest {
    private static final long EXISTED_ID = 1L;
    private static final long NON_EXISTED_ID = -1L;
    public static final long SOME_TEST_ID = 22L;
    public static final String MEMBER_MAIL_RU = "admin@mail.ru";
    public static final String PASSWORD = "12345";
    public static final String NOINDB_MAIL_RU = "noindb@mail.ru";
    public static final String INCORRECT_PASSWORD = "123";

    private final MemberRepository mockMemberRepository = mock(MemberRepository.class);
    private final CompanyRepository mockCompanyRepository =
            mock(CompanyRepository.class);
    private final Utils mockUtils = mock(Utils.class);
    private final SystemService mockSystemService = mock(SystemService.class);

    private MemberService memberService;

    private Member member;
    private Member followed;

    private static final Long EXIST_ID = 1L;

    @BeforeEach
    public void setUp() {
        memberService = new MemberServiceImpl(
                mockMemberRepository,
                mockCompanyRepository,
                mockUtils,
                mockSystemService);

        this.member = createMember();
        when(mockUtils.findMember(member)).thenReturn(member);
        this.followed = createMember();
        when(mockUtils.findMember(followed)).thenReturn(followed);
    }

    @Test
    @DisplayName("When member follow followed member, " +
            "then followed will be added to following list, " +
            "then member will be added to followers list")
    void testWhenMemberFollowsMember() {
        //when
        memberService.followMember(member, followed);
        memberService.followMember(member, followed);
        //then
        assertAll(
                () -> assertEquals(member.getFollowingMembers().get(0), followed),
                () -> assertEquals(followed.getFollowers().get(0), member)
        );
        verify(mockMemberRepository).save(member);
        verify(mockMemberRepository).save(followed);
    }

    @Test
    @DisplayName("When member unsubscribe from the followed member, " +
            "then followed will be removed from following list, " +
            "then member will be removed from followers list")
    void testWhenMemberUnFollowsMember() {
        //when
        addFollowerAndFollowed();
        memberService.unFollowMember(member, followed);
        //then
        assertAll(
                () -> assertEquals(member.getFollowingMembers().size(), 0),
                () -> assertEquals(followed.getFollowers().size(), 0)
        );
        verify(mockMemberRepository).save(member);
        verify(mockMemberRepository).save(followed);
    }

    @Test
    @DisplayName("When member send message, then it must de added " +
            "to his sent message list and " +
            "to recipients incoming messages lists")
    void testWhenMemberSentMessage() {
        //given
        Message message = createMessage();
        List<Member> recipients = message.getSendTo();
        //when
        memberService.sendMessage(message);
        //then
        assertEquals(member.getSentMessages().get(0), message);
        recipients.forEach(recipient ->
                assertEquals(recipient.getIncomingMessages().get(0), message));
        verify(mockMemberRepository).save(member);
        verify(mockSystemService).sendMessageNotification(recipients.get(0));
        verify(mockSystemService).sendMessageNotification(recipients.get(1));
        verify(mockSystemService).sendMessageNotification(recipients.get(2));
        verify(mockMemberRepository).saveMembers(message.getSendTo());
    }

    @Test
    @DisplayName("When member send message to yourself, " +
            "then incoming messages should contains his message")
    void testWhenMemberSentMessageToYourSelf() {
        //given
        Message message = createYourSelfMessage();
        //when
        memberService.sendMessage(message);
        //then
        assertEquals(member.getSentMessages().get(0), message);
        assertEquals(member.getIncomingMessages().get(0), message);
        verify(mockMemberRepository).save(member);
    }

    @Test
    @DisplayName("When member follow company, " +
            "then company should be added to members's list of following companies " +
            "and member should be added to company's list of followers")
    public void testFollowCompany() {
        //given
        Company company = createCompany();
        //when
        when(mockUtils.findCompany(company)).thenReturn(company);
        memberService.followCompany(member, company);
        memberService.followCompany(member, company);
        //then
        assertAll(
                () -> assertTrue(member.getFollowingCompanies().contains(company)),
                () -> assertTrue(company.getFollowers().contains(member))
        );
        verify(mockMemberRepository).save(member);
        verify(mockCompanyRepository).save(company);
    }

    @Test
    @DisplayName("When member unsubscribe from the company, " +
            "then company should be removed from member's list of following companies" +
            " and member should be removed from company's list of followers")
    public void testUnFollowCompany() {
        //given
        Company company = createCompany();
        company.getFollowers().add(member);
        member.getFollowingCompanies().add(company);
        //when
        when(mockUtils.findCompany(company)).thenReturn(company);
        memberService.unFollowCompany(member, company);
        //then
        assertAll(
                () -> assertFalse(member.getFollowingCompanies().contains(company)),
                () -> assertFalse(company.getFollowers().contains(member))
        );
        verify(mockMemberRepository).save(member);
        verify(mockCompanyRepository).save(company);
    }

    @Test
    @DisplayName("When contact was made, then member should be added " +
            "to another member's list of contacts")
    public void testAddContact() {
        //given
        Member anotherMember = createMember();
        //when
        memberService.addContact(member, anotherMember);
        memberService.addContact(member, anotherMember);
        //then
        assertTrue(member.getContacts().contains(anotherMember));
        verify(mockMemberRepository).save(member);
    }

    @Test
    @DisplayName("When all members gets, then should be called MemberRepository's getAll() method")
    public void testGetAllMembers() {
        //when
        memberService.getAllMembers();
        //then
        verify(mockMemberRepository).getAll();
    }

    @Test
    @DisplayName("When save member, then should be called MemberRepository's save() method with this member")
    public void testSaveMember() {
        //when
        memberService.save(member);
        //then
        verify(mockMemberRepository).save(member);
    }

    @Test
    @DisplayName("When get member, then it should be returned")
    public void testGetMemberById() {
        //when
        when(mockMemberRepository.getById(EXIST_ID)).thenReturn(Optional.of(member));
        Member result = memberService.getMemberById(EXIST_ID).get();
        //then
        assertEquals(member, result);
    }

    @Test
    @DisplayName("When get member by id and he exists in repository, " +
            "then return Optional of this member")
    public void testGetMemberByIdWhenHeExistsInRepo() {
        //when
        when(memberService.getMemberById(EXISTED_ID)).thenReturn(Optional.of(member));
        Optional<Member> memberOptional = memberService.getMemberById(member.getId());
        //then
        assertEquals(memberOptional, Optional.of(member));
        verify(mockMemberRepository).getById(EXISTED_ID);
    }

    @Test
    @DisplayName("When get member by id and he does not exist in repository, " +
            "then must be returned empty optional")
    public void testGetMemberByIdWhenHeDoesNotExistInRepo() {
        //when
        Optional<Member> memberOptional = memberService.getMemberById(NON_EXISTED_ID);
        //then
        assertEquals(memberOptional, Optional.empty());
        verify(mockMemberRepository).getById(NON_EXISTED_ID);
    }

    @Test
    @DisplayName("When delete member, then should be called MemberRepository's deleteMember() method with this member")
    public void testDeleteMember() {
        //when
        memberService.deleteMember(member);
        //then
        verify(mockMemberRepository).delete(member);
    }

    @Test
    @DisplayName("When member sign-in, then admin from db equals to our admin")
    public void memberLogin() {
        //when
        when(memberService.getAllMembers()).thenReturn(List.of(member));
        Optional<Member> loggedMember = memberService.login(member.getEmail(), member.getAccount().getPassword());
        //then
        assertEquals(member, loggedMember.get());
    }

    @Test
    @DisplayName("When member try to sign-in and email not correct, then return optional.empty")
    public void memberLoginWhenEmailIncorrect() {
        //when
        when(memberService.getAllMembers()).thenReturn(List.of(member));
        Optional<Member> loggedMember = memberService.login(NOINDB_MAIL_RU, member.getAccount().getPassword());
        //then
        assertTrue(loggedMember.isEmpty());
    }

    @Test
    @DisplayName("When member try to sign-in and password not correct, then return optional.empty")
    public void adminLoginWhenPasswordIncorrect() {
        //when
        when(memberService.getAllMembers()).thenReturn(List.of(member));
        Optional<Member> loggedMember = memberService.login(member.getEmail(), INCORRECT_PASSWORD);
        //then
        assertTrue(loggedMember.isEmpty());
    }

    private Message createMessage() {
        ArrayList<Member> recipients = new ArrayList<>();

        Message message = Message.builder()
                .author(member)
                .sendTo(recipients)
                .build();

        recipients.add(Member.builder().id(SOME_TEST_ID).build());
        recipients.add(Member.builder().id(SOME_TEST_ID).build());
        recipients.add(Member.builder().id(SOME_TEST_ID).build());

        return message;
    }

    private Message createYourSelfMessage() {
        ArrayList<Member> recipients = new ArrayList<>();

        Message message = Message.builder()
                .author(member)
                .sendTo(recipients)
                .build();

        recipients.add(Member.builder().id(member.getId()).build());

        return message;
    }

    private Member createMember() {
        Member member = Member.builder()
                .id(EXISTED_ID)
                .email(MEMBER_MAIL_RU)
                .build();
        member.setAccount(Account.builder().password(PASSWORD).build());
        return member;
    }

    private void addFollowerAndFollowed() {
        List<Member> followingMembers = member.getFollowingMembers();
        followingMembers.add(followed);
        member.setFollowingMembers(followingMembers);

        List<Member> followers = followed.getFollowers();
        followers.add(member);
        followed.setFollowers(followers);
    }

    private Company createCompany() {
        return Company.builder()
                .followers(new ArrayList<>())
                .postedJobs(new ArrayList<>())
                .build();
    }
}