package com.ra.course.linkedin.persistence.company;

import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.persistence.BaseRepository;

import java.util.List;

public interface CompanyRepository extends BaseRepository<Company> {
    List<Company> searchCompaniesByTitle(final String title);
}
