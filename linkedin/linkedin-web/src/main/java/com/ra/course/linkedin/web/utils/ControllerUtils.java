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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ControllerUtils {

    private final MemberService memberService;
    private final CompanyService companyService;
    private final AdminService adminService;
    private final JobService jobService;
    private final GroupService groupService;

    public Member getMemberById(final Long id) {
        return memberService.getMemberById(id).orElseThrow(
                () -> new EntityNotFoundException(Member.class.getSimpleName()));
    }

    public Company getCompanyById(final Long id) {
        return companyService.getCompanyById(id).orElseThrow(
                () -> new EntityNotFoundException(Company.class.getSimpleName()));
    }

    public Admin getAdminById(final Long id) {
        return adminService.getAdminById(id).orElseThrow(
                () -> new EntityNotFoundException(Admin.class.getSimpleName()));
    }

    public JobPosting getJobById(final Long id) {
        return jobService.getJobById(id).orElseThrow(
                () -> new EntityNotFoundException(JobPosting.class.getSimpleName()));
    }

    public Group getGroupById(final Long id) {
        return groupService.getGroupById(id).orElseThrow(
                () -> new EntityNotFoundException(Group.class.getSimpleName()));
    }
}
