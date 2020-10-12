package com.ra.course.linkedin.persistence.company;

import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.persistence.configuration.RepositoryConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = RepositoryConfiguration.class)
public class CompanyRepositoryImplIntegrationTest {

    @Autowired
    CompanyRepository companyRepository;

    @Test
    @DisplayName("When searched companies exist, then must be returned their list")
    void testWhenSearchedCompaniesExist() {
        //given
        String searchedTitle = "aAa";
        //when
        List<Company> foundCompanies =
                getSearchedCompanies(searchedTitle);
        //then
        assertAll(
                () -> assertEquals(foundCompanies.size(), 2),
                () -> assertEquals(foundCompanies.get(0).getName(),
                        createSearchedCompanies().get(0).getName()),
                () -> assertEquals(foundCompanies.get(1).getName(),
                        createSearchedCompanies().get(2).getName())
        );
    }

    @Test
    @DisplayName("When searched companies do not exist, " +
            "then must be returned empty list")
    void testWhenSearchedCompaniesDoNotExist() {
        //given
        String searchedTitle = "bBb";
        //when
        List<Company> foundCompanies =
                getSearchedCompanies(searchedTitle);
        //then
        assertEquals(foundCompanies.size(), 0);
    }

    private List<Company> createSearchedCompanies() {
        List<Company> searchedCompanies = new ArrayList<>();

        Company company = Company.builder()
                .name("aAaA")
                .build();
        Company company1 = Company.builder()
                .name("AbAa")
                .build();
        Company company2 = Company.builder()
                .name("BAaAb")
                .build();

        searchedCompanies.add(company);
        searchedCompanies.add(company1);
        searchedCompanies.add(company2);

        return searchedCompanies;
    }

    private List<Company> getSearchedCompanies(String searchedTitle) {
        List<Company> searchedCompanies = createSearchedCompanies();
        searchedCompanies.forEach(companyRepository::save);
        return companyRepository.searchCompaniesByTitle(searchedTitle);
    }
}