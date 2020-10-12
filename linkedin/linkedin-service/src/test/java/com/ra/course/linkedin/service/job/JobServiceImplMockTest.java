package com.ra.course.linkedin.service.job;

import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.model.entity.other.JobPosting;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.job.JobRepository;
import com.ra.course.linkedin.service.company.CompanyService;
import com.ra.course.linkedin.service.member.MemberService;
import com.ra.course.linkedin.service.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JobServiceImplMockTest {
    private JobService jobService;
    private final MemberService mockMemberService = mock(MemberService.class);
    private final CompanyService mockCompanyService = mock(CompanyService.class);
    private final JobRepository mockJobRepository = mock(JobRepository.class);
    private final Utils mockUtils = mock(Utils.class);
    private final Member member = new Member();
    private final JobPosting job = new JobPosting();
    private final Company company = new Company();

    private static final Long EXIST_ID = 1L;

    @BeforeEach
    public void before() {
        jobService = new JobServiceImpl(mockMemberService, mockCompanyService, mockJobRepository, mockUtils);
        when(mockUtils.findMember(member)).thenReturn(member);
        when(mockUtils.findCompany(company)).thenReturn(company);
    }

    @Test
    @DisplayName("When member save a job, then member with this job in list of saved jobs " +
            "should be saved in MemberRepository")
    public void testSaveJob() {
        //when
        jobService.saveJob(member, job);
        jobService.saveJob(member, job);
        //then
        assertTrue(member.getSavedJobs().contains(job));
        verify(mockMemberService).save(member);
    }

    @Test
    @DisplayName("When member delete a job, then in member's list of saved jobs should be absent this job " +
            "and this member should be saved in MemberRepository")
    public void testDeleteJob() {
        //when
        jobService.deleteJobFromSaved(member, job);
        //then
        assertFalse(member.getSavedJobs().contains(job));
        verify(mockMemberService).save(member);
    }

    @Test
    @DisplayName("When member apply for job, then this job should be added to member's list of applied jobs and " +
            "to company's list of applied jobs")
    public void testApplyForJob() {
        //given
        company.setAppliedJobs(new ArrayList<>());
        job.setCompany(company);
        //when
        when(mockUtils.findCompany(company)).thenReturn(company);
        jobService.applyForJob(member, job);
        jobService.applyForJob(member, job);
        //then
        assertTrue(member.getAppliedJobPosting().contains(job));
        assertTrue(company.getAppliedJobs().contains(job));
        verify(mockMemberService).save(member);
        verify(mockCompanyService, times(2))
                .addOrUpdateCompany(company);
    }

    @Test
    @DisplayName("When added a job, then should be called JobRepository's save() method")
    public void addJobPosting() {
        //given
        company.setId(EXIST_ID);
        job.setCompany(company);
        //when
        when(mockCompanyService.getCompanyById(EXIST_ID)).thenReturn(Optional.of(company));
        jobService.addJobPosting(job);
        //then
        verify(mockJobRepository).save(job);
    }

    @Test
    @DisplayName("When get job by id, then it should be returned")
    public void testGetJobById() {
        //when
        when(mockJobRepository.getById(EXIST_ID)).thenReturn(Optional.of(job));
        JobPosting result = jobService.getJobById(EXIST_ID).get();
        //then
        assertEquals(job, result);
    }

    @Test
    @DisplayName("When get all jobs, then should be returned list of all jobs")
    public void testGetAllJobs() {
        //when
        when(mockJobRepository.getAll()).thenReturn(List.of(job));
        List<JobPosting> result = jobService.getAllJobs();
        //then
        assertEquals(List.of(job), result);
    }

    @Test
    @DisplayName("When get jobs with specific companyId, " +
            "then return list of jobs with this companyId")
    public void testGetJobsByCompanyId() {
        //when
        when(mockJobRepository.getJobsByCompanyId(EXIST_ID))
                .thenReturn(List.of(job));
        List<JobPosting> result = jobService.getJobsByCompanyId(EXIST_ID);
        //then
        assertEquals(List.of(job), result);
    }

    @Test
    @DisplayName("When get jobs with specific member and member exists, " +
            "then return list of jobs with this member")
    public void testGetJobsByMemberIdWhenMemberExists() {
        //when
        when(mockJobRepository.getJobsByMember(member))
                .thenReturn(List.of(job));
        when(mockMemberService.getMemberById(member.getId()))
                .thenReturn(Optional.of(member));
        List<JobPosting> result = jobService.getJobsByMemberId(member.getId());
        //then
        assertEquals(List.of(job), result);
    }

    @Test
    @DisplayName("When get jobs with specific member and member " +
            "does mot exists, then return new ArrayList")
    public void testGetJobsByMemberIdWhenMemberDoesNotExist() {
        //when
        when(mockJobRepository.getJobsByMember(member))
                .thenReturn(new ArrayList<>());
        List<JobPosting> result = jobService.getJobsByMemberId(member.getId());
        //then
        assertEquals(new ArrayList<>(), result);
    }
}
