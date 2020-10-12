package com.ra.course.linkedin.service.search;

import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.model.entity.other.JobPosting;
import com.ra.course.linkedin.model.entity.person.Member;

import java.util.List;


public interface SearchService {
    List<Member> searchMembersByName(String name);

    List<Company> searchCompaniesByTitle(String title);

    List<JobPosting> searchJobsByEmploymentType(String employmentType);
}
