package com.ra.course.linkedin.persistence.job;

import com.ra.course.linkedin.model.entity.other.JobPosting;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Repository
public class JobRepositoryImpl extends BaseRepositoryImpl<JobPosting>
        implements JobRepository {
    @Override
    public List<JobPosting> searchJobsByEmploymentType(final String employmentType) {
        return repository.stream()
                .filter(jobPosting -> jobPosting
                        .getEmploymentType().toLowerCase(Locale.ROOT)
                        .contains(employmentType.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
    }

    @Override
    public List<JobPosting> getJobsByCompanyId(final Long id) {
        return repository.stream()
                .filter(jobPosting -> jobPosting
                        .getCompany().getId().equals(id))
                .collect(Collectors.toList());
    }

    @Override
    public List<JobPosting> getJobsByMember(final Member member) {
        return repository.stream()
                .filter(jobPosting -> jobPosting
                        .getMembers().contains(member))
                .collect(Collectors.toList());
    }
}
