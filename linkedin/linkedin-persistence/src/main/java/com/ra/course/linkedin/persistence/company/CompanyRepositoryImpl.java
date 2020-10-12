package com.ra.course.linkedin.persistence.company;

import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.persistence.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Repository
public class CompanyRepositoryImpl extends BaseRepositoryImpl<Company>
        implements CompanyRepository {

    @Override
    public List<Company> searchCompaniesByTitle(final String title) {
        return repository.stream()
                .filter(company -> company.getName().toLowerCase(Locale.ROOT)
                        .contains(title.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
    }
}
