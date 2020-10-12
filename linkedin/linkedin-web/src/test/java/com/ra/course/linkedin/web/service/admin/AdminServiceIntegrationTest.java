package com.ra.course.linkedin.web.service.admin;

import com.ra.course.linkedin.model.entity.person.Account;
import com.ra.course.linkedin.model.entity.person.AccountStatus;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.member.MemberRepository;
import com.ra.course.linkedin.service.admin.AdminService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.ra.course.linkedin.web.service.ServiceIntegrationTestUtils.tryToDelete;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AdminServiceIntegrationTest {

    @Autowired
    private AdminService adminService;
    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private Account account;

    @BeforeEach
    void setUp() {
        member = createMember();
        account = member.getAccount();
        memberRepository.save(member);
    }

    @AfterEach
    void tearDown() {
        tryToDelete(memberRepository, member);
    }

    @Test
    @DisplayName("When admin block member, this member must have " +
            "blocked account status")
    void testBlockMember() {
        //given
        account.setStatus(AccountStatus.ACTIVE);
        memberRepository.save(member);
        //when
        adminService.blockMember(member);
        //then
        assertEquals(memberRepository.getById(member.getId()).get()
                .getAccount().getStatus(), AccountStatus.BLOCKED);
    }

    @Test
    @DisplayName("When admin unblock member, this member must have " +
            "unblocked account status")
    void testUnBlockMember() {
        //given
        account.setStatus(AccountStatus.BLOCKED);
        memberRepository.save(member);
        //when
        adminService.unblockMember(member);
        //then
        assertEquals(memberRepository.getById(member.getId()).get()
                .getAccount().getStatus(), AccountStatus.ACTIVE);
    }

    private Member createMember() {
        return Member.builder()
                .account(new Account())
                .build();
    }
}
