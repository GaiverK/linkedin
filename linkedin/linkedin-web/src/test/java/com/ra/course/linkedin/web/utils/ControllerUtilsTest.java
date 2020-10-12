package com.ra.course.linkedin.web.utils;

import com.ra.course.linkedin.model.entity.exceptions.EntityNotFoundException;
import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.model.entity.other.Group;
import com.ra.course.linkedin.model.entity.other.JobPosting;
import com.ra.course.linkedin.model.entity.person.Admin;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.service.admin.AdminService;
import com.ra.course.linkedin.service.company.CompanyService;
import com.ra.course.linkedin.service.group.GroupService;
import com.ra.course.linkedin.service.job.JobService;
import com.ra.course.linkedin.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ControllerUtilsTest {

    public static final long NON_EXISTENT_ID = -1L;
    public static final String MEMBER_NAME = "Vasya";
    public static final String COMPANY_NAME = "Company";
    public static final String LOCATION = "Any Location";
    public static final String GROUP_NAME = "Group name";
    @Autowired
    private ControllerUtils controllerUtils;
    @Autowired
    private MemberService memberService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private JobService jobService;
    @Autowired
    private GroupService groupService;

    private Member member;
    private Company company;
    private Admin admin;
    private JobPosting job;
    private Group group;

    @BeforeEach
    void setUp() {
        member = getMember();
        company = getCompany();
        admin = getAdmin();
        job = getJob();
        group = getGroup();
    }

    @Test
    @DisplayName("When Member not exists then throw Exception")
    void getMemberByIdWhenMemberNotExists() {
        //then
        assertThrows(EntityNotFoundException.class, () -> controllerUtils.getMemberById(NON_EXISTENT_ID));
    }

    @Test
    @DisplayName("When Member exists then does not throw Exception")
    void getMemberByIdWhenMemberExists() {
        //given
        memberService.save(member);
        //then
        assertDoesNotThrow(() -> controllerUtils.getMemberById(member.getId()));
        memberService.deleteMember(member);
    }

    @Test
    @DisplayName("When Company not exists then throw Exception")
    void getCompanyByIdWhenCompanyNotExists() {
        //then
        assertThrows(EntityNotFoundException.class, () -> controllerUtils.getCompanyById(NON_EXISTENT_ID));
    }

    @Test
    @DisplayName("When Company exists then does not throw Exception")
    void getCompanyByIdWhenCompanyExists() {
        //given
        companyService.addOrUpdateCompany(company);
        //then
        assertDoesNotThrow(() -> controllerUtils.getCompanyById(company.getId()));
        companyService.deleteCompany(company);
    }

    @Test
    @DisplayName("When Admin not exists then throw Exception")
    void getAdminByIdWhenAdminNotExists() {
        //then
        assertThrows(EntityNotFoundException.class, () -> controllerUtils.getAdminById(NON_EXISTENT_ID));
    }

    @Test
    @DisplayName("When Admin exists then does not throw Exception")
    void getAdminByIdWhenAdminExists() {
        //given
        adminService.save(admin);
        //then
        assertDoesNotThrow(() -> controllerUtils.getAdminById(admin.getId()));
        adminService.deleteAdmin(admin);
    }

    @Test
    @DisplayName("When Job not exists then throw Exception")
    void getJobByIdWhenJobNotExists() {
        //then
        assertThrows(EntityNotFoundException.class, () -> controllerUtils.getJobById(NON_EXISTENT_ID));
    }

    @Test
    @DisplayName("When Job exists then does not throw Exception")
    void getJobByIdWhenJobExists() {
        //given
        getJobByIdWhenJobExistsAllData();
        //then
        assertDoesNotThrow(() -> controllerUtils.getJobById(job.getId()));
        jobService.deleteJobFromSaved(member, job);
        memberService.deleteMember(member);
        companyService.deleteCompany(company);
    }

    @Test
    @DisplayName("When Group not exists then throw Exception")
    void getGroupByIdWhenGroupExists() {
        //then
        assertThrows(EntityNotFoundException.class, () -> controllerUtils.getGroupById(NON_EXISTENT_ID));
    }

    @Test
    @DisplayName("When Group exists then does not throw Exception")
    void getGroupByIdWhenGroupNotExists() {
        //given
        memberService.save(member);
        group.setOwner(member);
        groupService.addGroupToRepo(group);
        //then
        assertDoesNotThrow(() -> controllerUtils.getGroupById(group.getId()));
        groupService.deleteGroup(group);
    }

    private void getJobByIdWhenJobExistsAllData() {
        memberService.save(member);
        company.setPostedJobs(new ArrayList<>());
        company.getPostedJobs().add(job);
        companyService.addOrUpdateCompany(company);
        job.setCompany(company);
        jobService.addJobPosting(job);
    }

    private Member getMember() {
        return Member.builder()
                .name(MEMBER_NAME)
                .build();
    }

    private Company getCompany() {
        return Company.builder()
                .name(COMPANY_NAME)
                .build();
    }

    private Admin getAdmin() {
        Admin admin = new Admin();
        admin.setName(MEMBER_NAME);
        return admin;
    }

    private JobPosting getJob() {
        return JobPosting.builder()
                .location(LOCATION)
                .build();
    }

    private Group getGroup() {
        return Group.builder()
                .name(GROUP_NAME)
                .build();
    }
}