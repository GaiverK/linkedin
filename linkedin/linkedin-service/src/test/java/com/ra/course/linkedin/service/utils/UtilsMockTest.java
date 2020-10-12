package com.ra.course.linkedin.service.utils;

import com.ra.course.linkedin.model.entity.exceptions.EntityNotFoundException;
import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.model.entity.other.ConnectionInvitation;
import com.ra.course.linkedin.model.entity.other.Post;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.company.CompanyRepository;
import com.ra.course.linkedin.persistence.invitation.InvitationRepository;
import com.ra.course.linkedin.persistence.member.MemberRepository;
import com.ra.course.linkedin.persistence.post.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UtilsMockTest {

    private Utils utils;
    private final MemberRepository mockMemberRepository = mock(MemberRepository.class);
    private final CompanyRepository mockCompanyRepository =
            mock(CompanyRepository.class);
    private final InvitationRepository mockInvitationRepository =
            mock(InvitationRepository.class);
    private final PostRepository mockPostRepository = mock(PostRepository.class);

    @BeforeEach
    public void before() {
        utils = new Utils(mockMemberRepository,
                mockCompanyRepository,
                mockInvitationRepository,
                mockPostRepository);
    }

    @Test
    @DisplayName("When member exist in repository, then return this member")
    public void testFindMemberWhenMemberExist() {
        //given
        Member member = createMember();
        //when
        when(mockMemberRepository.getById(member.getId()))
                .thenReturn(Optional.of(member));
        Member actual = utils.findMember(member);
        //then
        assertEquals(actual, member);
    }

    @Test
    @DisplayName("When member is not exist in repository, then throw EntityNotFoundException")
    public void testFindMemberWhenMemberNotExist() {
        //given
        Member member = createMember();
        //when
        when(mockMemberRepository.getById(member.getId()))
                .thenReturn(Optional.empty());
        //then
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> utils.findMember(member));
        assertTrue(thrown.getMessage().contains(Member.class.getSimpleName()));
    }

    @Test
    @DisplayName("When company exist in repository, then return this company")
    public void testFindCompanyWhenCompanyExist() {
        //given
        Company company = createCompany();
        //when
        when(mockCompanyRepository.getById(company.getId()))
                .thenReturn(Optional.of(company));
        Company actual = utils.findCompany(company);
        //then
        assertEquals(actual, company);
    }

    @Test
    @DisplayName("When company is not exist in repository, then throw EntityNotFoundException")
    public void testFindCompanyWhenCompanyNotExist() {
        //given
        Company company = createCompany();
        //when
        when(mockCompanyRepository.getById(company.getId()))
                .thenReturn(Optional.empty());
        //then
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> utils.findCompany(company));
        assertTrue(thrown.getMessage().contains(Company.class.getSimpleName()));
    }

    @Test
    @DisplayName("When connection invitation exist in repository, then return this invitation")
    public void testFindInvitationWhenInvitationExist() {
        //given
        ConnectionInvitation invitation = createInvitation();
        //when
        when(mockInvitationRepository.getById(invitation.getId()))
                .thenReturn(Optional.of(invitation));
        ConnectionInvitation actual = utils.findInvitation(invitation);
        //then
        assertEquals(actual, invitation);
    }

    @Test
    @DisplayName("When connection invitation is not exist in repository, then throw EntityNotFoundException")
    public void testFindInvitationWhenInvitationNotExist() {
        //given
        ConnectionInvitation invitation = createInvitation();
        //when
        when(mockInvitationRepository.getById(invitation.getId()))
                .thenReturn(Optional.empty());
        //then
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> utils.findInvitation(invitation));
        assertTrue(thrown.getMessage().contains(ConnectionInvitation.class.getSimpleName()));
    }

    @Test
    @DisplayName("When post exist in repository, then return this post")
    public void testFindPostWhenPostExist() {
        //given
        Post post = createPost();
        //when
        when(mockPostRepository.getById(post.getId()))
                .thenReturn(Optional.of(post));
        Post actual = utils.findPost(post);
        //then
        assertEquals(actual, post);
    }

    @Test
    @DisplayName("When post is not exist in repository, then throw EntityNotFoundException")
    public void testFindPostWhenPostNotExist() {
        //given
        Post post = createPost();
        //when
        when(mockPostRepository.getById(post.getId()))
                .thenReturn(Optional.empty());
        //then
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> utils.findPost(post));
        assertTrue(thrown.getMessage().contains(Post.class.getSimpleName()));
    }

    private Member createMember() {
        Member member = Member.builder()
                .id(1L)
                .build();
        member.setName("member");
        return member;
    }

    private Company createCompany() {
        return Company.builder()
                .id(1L)
                .name("company")
                .build();
    }

    private ConnectionInvitation createInvitation() {
        return ConnectionInvitation.builder()
                .id(1L)
                .build();
    }

    private Post createPost() {
        return Post.builder()
                .id(1L)
                .build();
    }
}
