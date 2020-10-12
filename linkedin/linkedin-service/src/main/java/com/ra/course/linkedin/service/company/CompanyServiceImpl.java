package com.ra.course.linkedin.service.company;

import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.persistence.company.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    public Company addOrUpdateCompany(final Company company) {
        return companyRepository.save(company);
    }

    @Override
    public List<Company> getAllCompanies() {
        return StreamSupport.stream(companyRepository.getAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Company> getCompanyById(final Long id) {
        return companyRepository.getById(id);
    }

    @Override
    public void deleteCompany(final Company company) {
        companyRepository.delete(company);
    }
}
