package com.ra.course.linkedin.web.service.company;

import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.persistence.company.CompanyRepository;
import com.ra.course.linkedin.service.company.CompanyService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.ra.course.linkedin.web.service.ServiceIntegrationTestUtils.tryToDelete;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CompanyServiceIntegrationTest {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyRepository companyRepository;

    private Company company;

    @BeforeEach
    public void before() {
        company = new Company();
    }

    @AfterEach
    public void after() {
        tryToDelete(companyRepository, company);
    }

    @Test
    @DisplayName("When company added or updated, then this company should be present in repository")
    public void testAddOrUpdateCompany() {
        //when
        companyService.addOrUpdateCompany(company);
        //then
        assertTrue(companyService.getAllCompanies().contains(company));
    }

    @Test
    @DisplayName("When get all companies, then should be returns List of companies")
    public void testGetAllCompanies() {
        //given
        companyService.addOrUpdateCompany(company);
        //when
        List<Company> companies = companyService.getAllCompanies();
        //then
        assertTrue(companies.contains(company));
    }
}
