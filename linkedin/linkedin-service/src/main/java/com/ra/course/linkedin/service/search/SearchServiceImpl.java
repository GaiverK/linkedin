package com.ra.course.linkedin.service.search;

import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.model.entity.other.JobPosting;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.company.CompanyRepository;
import com.ra.course.linkedin.persistence.job.JobRepository;
import com.ra.course.linkedin.persistence.member.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;

    @Override
    public List<Member> searchMembersByName(final String name) {
        return memberRepository.searchMembersByName(name);
    }

    @Override
    public List<Company> searchCompaniesByTitle(final String title) {
        return companyRepository.searchCompaniesByTitle(title);
    }

    @Override
    public List<JobPosting> searchJobsByEmploymentType(final String employmentType) {
        return jobRepository.searchJobsByEmploymentType(employmentType);
    }
}
