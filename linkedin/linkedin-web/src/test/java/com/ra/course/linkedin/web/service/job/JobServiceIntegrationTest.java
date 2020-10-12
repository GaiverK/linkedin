package com.ra.course.linkedin.web.service.job;

import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.model.entity.other.JobPosting;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.company.CompanyRepository;
import com.ra.course.linkedin.persistence.job.JobRepository;
import com.ra.course.linkedin.persistence.member.MemberRepository;
import com.ra.course.linkedin.service.company.CompanyService;
import com.ra.course.linkedin.service.job.JobService;
import com.ra.course.linkedin.service.member.MemberService;
import com.ra.course.linkedin.service.utils.Utils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static com.ra.course.linkedin.web.service.ServiceIntegrationTestUtils.tryToDelete;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JobServiceIntegrationTest {

    @Autowired
    private JobService jobService;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private Utils utils;

    private JobPosting job;
    private Member member;
    private Company company;

    @BeforeEach
    public void before() {
        company = companyService.addOrUpdateCompany(createCompany());
        member = memberService.save(createMember());
        job = createJob();
    }

    @AfterEach
    public void after() {
        tryToDelete(jobRepository, job);
        tryToDelete(memberRepository, member);
        tryToDelete(companyRepository, company);
    }

    @Test
    @DisplayName("When job added, then this job should be present in repository")
    public void testAddJobPosting() {
        //when
        jobService.addJobPosting(job);
        //then
        assertTrue(jobService.getAllJobs().contains(job));
    }

    @Test
    @DisplayName("When get all jobs, then should be returns List of jobs")
    public void testGetAllJobs() {
        //given
        jobService.addJobPosting(job);
        //when
        List<JobPosting> jobs = jobService.getAllJobs();
        //then
        assertTrue(jobs.contains(job));
    }

    @Test
    @DisplayName("When member save job, then this job should be present in member's list of saved jobs")
    public void testSaveJob() {
        //when
        jobService.saveJob(member, job);
        //then
        assertTrue(utils.findMember(member).getSavedJobs().contains(job));
    }

    @Test
    @DisplayName("When member delete job, then this job should be absent in member's list of saved jobs")
    public void testDeleteJob() {
        //given
        jobService.saveJob(member, job);
        //when
        jobService.deleteJobFromSaved(member, job);
        //then
        assertFalse(utils.findMember(member).getSavedJobs().contains(job));
    }

    @Test
    @DisplayName("When member apply for job, " +
            "then this job should be present in member's and company's list of applied jobs")
    public void testApplyForJob() {
        //given
        job.setCompany(company);
        //when
        jobService.applyForJob(member, job);
        //then
        assertAll(
                () -> assertTrue(utils.findMember(member).getAppliedJobPosting().contains(job)),
                () -> assertTrue(utils.findCompany(company).getAppliedJobs().contains(job))
        );
    }

    private JobPosting createJob() {
        return JobPosting.builder()
                .company(company)
                .build();
    }

    private Member createMember() {
        return Member.builder().build();
    }

    private Company createCompany() {
        return Company.builder()
                .appliedJobs(new ArrayList<>())
                .postedJobs(new ArrayList<>())
                .build();
    }
}
