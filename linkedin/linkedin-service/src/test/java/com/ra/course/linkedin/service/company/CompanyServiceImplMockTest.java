package com.ra.course.linkedin.service.company;

import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.persistence.company.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CompanyServiceImplMockTest {
    private CompanyService companyService;
    private final CompanyRepository mockCompanyRepository = mock(CompanyRepository.class);

    private static final Long EXIST_ID = 1L;

    private Company company;

    @BeforeEach
    public void before() {
        companyService = new CompanyServiceImpl(mockCompanyRepository);
        company = new Company();
    }

    @Test
    @DisplayName("When the company added or updated, " +
            "then should be called CompanyRepository's save method")
    public void testAddOrUpdateCompany() {
        //when
        companyService.addOrUpdateCompany(company);
        //then
        verify(mockCompanyRepository).save(company);
    }

    @Test
    @DisplayName("When get all companies, then should be called CompanyRepository's getAll() method")
    public void testGetAllCompanies() {
        //when
        companyService.getAllCompanies();
        //then
        verify(mockCompanyRepository).getAll();
    }

    @Test
    @DisplayName("When get company by id, then it should be returned")
    public void testGetCompanyById() {
        //when
        when(mockCompanyRepository.getById(EXIST_ID)).thenReturn(Optional.of(company));
        Company result = companyService.getCompanyById(EXIST_ID).get();
        //then
        assertEquals(company, result);
    }

    @Test
    @DisplayName("When delete company, then should be called CompanyRepository's deleteCompany() method")
    public void testDeleteCompany() {
        //when
        companyService.deleteCompany(company);
        //then
        verify(mockCompanyRepository).delete(company);
    }
}
