package com.ra.course.linkedin.service.job;

import com.ra.course.linkedin.model.entity.other.JobPosting;
import com.ra.course.linkedin.model.entity.person.Member;

import java.util.List;
import java.util.Optional;

public interface JobService {
    void saveJob(Member member, JobPosting jobPosting);

    void deleteJobFromSaved(Member member, JobPosting jobPosting);

    void applyForJob(Member member, JobPosting jobPosting);

    JobPosting addJobPosting(JobPosting jobPosting);

    List<JobPosting> getAllJobs();

    List<JobPosting> getJobsByCompanyId(Long id);

    List<JobPosting> getJobsByMemberId(Long id);

    Optional<JobPosting> getJobById(Long id);

    void deleteJob(JobPosting jobPosting);
}
