package com.ra.course.linkedin.web.controller.company;

import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.model.entity.other.JobPosting;
import com.ra.course.linkedin.service.company.CompanyService;
import com.ra.course.linkedin.service.job.JobService;
import com.ra.course.linkedin.web.utils.ControllerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static com.ra.course.linkedin.web.controller.profile.ProfileControllerMockMvcTest.SOME_ID_22;
import static com.ra.course.linkedin.web.controller.recommendation.RecommendationControllerMockMvcTest.SOME_ID_33;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CompanyService companyService;
    @MockBean
    private JobService jobService;
    @MockBean
    private ControllerUtils controllerUtils;

    private Company company;

    @BeforeEach
    void setUp() {
        company = createCompany();
    }

    @Test
    @DisplayName("Expect status is Ok and model has attribute company")
    public void addNewCompany_getTest() throws Exception {
        //then
        mockMvc.perform(get("/companies/add-new-company"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("company"))
                .andExpect(view().name("company/add-new-company"));
    }

    @Test
    @DisplayName("Expect status is Redirection, redirected url is" +
            "/companies/get-all-companies " +
            " and companyService used method addOrUpdateCompany")
    public void addNewCompany_postTest() throws Exception {
        //then
        mockMvc.perform(post("/companies/add-new-company")
                .param("name", "company"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/companies/get-all-companies"));

        verify(companyService).addOrUpdateCompany(any(Company.class));
    }

    @Test
    @DisplayName("Expect status is Ok, model has attribute companies and " +
            "companyService used method getAllCompanies")
    public void getAllCompaniesTest() throws Exception {
        //then
        mockMvc.perform(get("/companies/get-all-companies"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("companies"))
                .andExpect(view().name("company/companies-list"));

        verify(companyService).getAllCompanies();
    }

    @Test
    @DisplayName("Expect status is Ok and model has attribute updateCompany")
    public void updateCompany_getTest() throws Exception {
        //when
        when(controllerUtils.getCompanyById(company.getId())).thenReturn(company);
        //then
        mockMvc.perform(
                get("/companies/update-company/" + company.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("updateCompany"))
                .andExpect(view().name("company/update-company"));
    }

    @Test
    @DisplayName("Expect status is Redirection and " +
            "redirected url is /companies/get-all-companies;" +
            "companyService used method  addOrUpdateCompany with parameter company")
    public void updateCompany_postTest() throws Exception {
        //when
        when(controllerUtils.getCompanyById(company.getId())).thenReturn(company);
        //then
        mockMvc.perform(
                post("/companies/update-company/" + company.getId())
                        .param("name", "new company"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/companies/get-all-companies"));

        verify(companyService).addOrUpdateCompany(company);
    }

    @Test
    @DisplayName("Expect status is Ok and model has attributes " +
            "company and job.")
    public void getCompanyPageTest() throws Exception {
        //when
        company.getPostedJobs().add(createJob());
        company.getPostedJobs().add(createJob());
        when(controllerUtils.getCompanyById(company.getId())).thenReturn(company);
        //then
        mockMvc.perform(get("/companies/" + company.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("company"))
                .andExpect(view().name("company/company-page"));
    }

    @Test
    @DisplayName("Expect status is Redirection and " +
            "redirected url is: /companies/{company.getId()}; " +
            "jobService used method addJobPosting")
    public void addJobTest() throws Exception {
        //when
        JobPosting job = createJob();
        when(controllerUtils.getCompanyById(company.getId())).thenReturn(company);
        when(controllerUtils.getJobById(job.getId())).thenReturn(job);
        //then
        mockMvc.perform(post("/companies/" + company.getId() + "/jobs/add-job")
                .param("description", job.getDescription()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/companies/" + company.getId()));

        verify(jobService).addJobPosting(any(JobPosting.class));
    }

    private Company createCompany() {
        return Company.builder()
                .id(SOME_ID_22)
                .followers(new ArrayList<>())
                .postedJobs(new ArrayList<>())
                .appliedJobs(new ArrayList<>())
                .build();
    }

    private JobPosting createJob() {
        return JobPosting.builder()
                .id(SOME_ID_33)
                .description("description")
                .build();
    }
}
