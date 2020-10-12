package com.ra.course.linkedin.service.company;

import com.ra.course.linkedin.model.entity.other.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
    Company addOrUpdateCompany(Company company);
    List<Company> getAllCompanies();
    Optional<Company> getCompanyById(Long id);
    void deleteCompany(Company company);
}
