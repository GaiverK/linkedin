package com.ra.course.linkedin.service.admin;

import com.ra.course.linkedin.model.entity.person.Account;
import com.ra.course.linkedin.model.entity.person.AccountStatus;
import com.ra.course.linkedin.model.entity.person.Admin;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.admin.AdminRepository;
import com.ra.course.linkedin.persistence.member.MemberRepository;
import com.ra.course.linkedin.service.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class AdminServiceImplMockTest {
    public static final String ADMIN_MAIL_RU = "admin@mail.ru";
    public static final String PASSWORD = "12345";
    public static final String NOINDB_MAIL_RU = "noindb@mail.ru";
    public static final String INCORRECT_PASSWORD = "123";
    private AdminService adminService;
    private final AdminRepository mockAdminRepository = mock(AdminRepository.class);
    private final MemberRepository mockMemberRepository = mock(MemberRepository.class);
    private final Utils mockUtils = mock(Utils.class);
    private final Member member = new Member();
    private final Account account = new Account();
    private Admin admin;

    private static final Long EXIST_ID = 1L;

    @BeforeEach
    public void before() {
        adminService = new AdminServiceImpl(mockAdminRepository, mockMemberRepository, mockUtils);
        admin = createAdmin();
    }

    @Test
    @DisplayName("When admin blocks member, then member's account has status - blocked")
    public void testBlockMember() {
        //given
        account.setStatus(AccountStatus.ACTIVE);
        member.setAccount(account);
        //when
        when(mockUtils.findMember(member)).thenReturn(member);
        adminService.blockMember(member);
        //then
        assertEquals(member.getAccount().getStatus(), AccountStatus.BLOCKED);
        verify(mockMemberRepository).save(member);
    }

    @Test
    @DisplayName("When admin unblocks member, then member's account has status - active")
    public void testUnblockMember() {
        //given
        account.setStatus(AccountStatus.BLOCKED);
        member.setAccount(account);
        //when
        when(mockUtils.findMember(member)).thenReturn(member);
        adminService.unblockMember(member);
        //then
        assertEquals(member.getAccount().getStatus(), AccountStatus.ACTIVE);
        verify(mockMemberRepository).save(member);
    }

    @Test
    @DisplayName("When register new admin, then should be called AdminRepository's save() method with this admin")
    public void testAdminSave() {
        //when
        adminService.save(admin);
        //then
        verify(mockAdminRepository).save(admin);
    }

    @Test
    @DisplayName("When get admin by id, then it should be returned")
    public void testGetAdminById() {
        //when
        when(mockAdminRepository.getById(EXIST_ID)).thenReturn(Optional.of(admin));
        Admin result = adminService.getAdminById(EXIST_ID).get();
        //then
        assertEquals(admin, result);
    }

    @Test
    @DisplayName("When get all admins, should be return list of all admins")
    public void testGetAllAdmins() {
        //when
        when(mockAdminRepository.getAll()).thenReturn(List.of(admin));
        List<Admin> result = adminService.getAllAdmins();
        //then
        assertEquals(List.of(admin), result);
    }

    @Test
    @DisplayName("When admin sign-in, then admin from db equals to our admin")
    public void adminLogin() {
        //when
        when(mockAdminRepository.getAll()).thenReturn(List.of(admin));
        Optional<Admin> loggedAdmin = adminService.login(admin.getEmail(), admin.getAccount().getPassword());
        //then
        assertEquals(admin, loggedAdmin.get());
    }

    @Test
    @DisplayName("When admin try to sign-in and email not correct, then return optional.empty")
    public void adminLoginWhenEmailIncorrect() {
        //when
        when(mockAdminRepository.getAll()).thenReturn(List.of(admin));
        Optional<Admin> loggedAdmin = adminService.login(NOINDB_MAIL_RU, admin.getAccount().getPassword());
        //then
        assertTrue(loggedAdmin.isEmpty());
    }

    @Test
    @DisplayName("When admin try to sign-in and password not correct, then return optional.empty")
    public void adminLoginWhenPasswordIncorrect() {
        //when
        when(mockAdminRepository.getAll()).thenReturn(List.of(admin));
        Optional<Admin> loggedAdmin = adminService.login(admin.getEmail(), INCORRECT_PASSWORD);
        //then
        assertTrue(loggedAdmin.isEmpty());
    }

    @Test
    @DisplayName("When delete admin, then should be called AdminRepository's deleteAdmin() method with this admin")
    public void testDeleteAdmin() {
        //when
        adminService.deleteAdmin(admin);
        //then
        verify(mockAdminRepository).delete(admin);
    }

    private Admin createAdmin() {
        Admin admin = new Admin();
        admin.setId(EXIST_ID);
        admin.setEmail(ADMIN_MAIL_RU);
        admin.setAccount(Account.builder().password(PASSWORD).build());
        return admin;
    }
}
