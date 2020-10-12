package com.ra.course.linkedin.persistence.job;

import com.ra.course.linkedin.model.entity.other.JobPosting;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.BaseRepository;

import java.util.List;

public interface JobRepository extends BaseRepository<JobPosting> {
    List<JobPosting> searchJobsByEmploymentType(final String employmentType);

    List<JobPosting> getJobsByCompanyId(final Long id);

    List<JobPosting> getJobsByMember(final Member member);
}
