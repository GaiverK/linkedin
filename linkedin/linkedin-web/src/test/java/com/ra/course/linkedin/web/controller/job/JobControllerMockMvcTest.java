package com.ra.course.linkedin.web.controller.job;

import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.model.entity.other.JobPosting;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.service.job.JobService;
import com.ra.course.linkedin.web.dto.job.JobPostingDto;
import com.ra.course.linkedin.web.utils.ControllerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class JobControllerMockMvcTest {

    public static final String STRING_TEST_ID = "1";
    public static final String EXPECTED_REDIRECTED_URL = "/profiles/get/" + STRING_TEST_ID;
    public static final String MEMBER_NAME = "Pushkin";
    public static final String COMPANY_NAME = "Hillel";
    public static final String JOB_LOCATION = "Some street";
    public static final String JOB_NAME = "Some job";
    public static final long DEFAULT_ID = 1L;
    public static final int COMPANY_SIZE = 5;
    @MockBean
    private JobService jobService;
    @MockBean
    private ControllerUtils controllerUtils;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    private JobPosting jobPosting;
    private Member member;
    private Company company;

    @BeforeEach
    void setUp() {
        company = createCompany();
        member = createMember();
        jobPosting = createJob();
    }

    @Test
    @DisplayName("Expect status is ok, model has attribute jobs")
    public void testGetJobs() throws Exception {
        //when
        when(jobService.getAllJobs()).thenReturn(List.of(jobPosting));
        //then
        mockMvc.perform(get("/jobs/all"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("jobs",
                        jobService.getAllJobs().stream()
                                .map(jobPosting -> modelMapper.map(jobPosting,
                                        JobPostingDto.class))
                                .collect(Collectors.toList())));
    }

    @Test
    @DisplayName("Will redirect to members after applying job")
    void applyJob() throws Exception {
        //when
        when(controllerUtils.getMemberById(anyLong())).thenReturn(member);
        when(controllerUtils.getJobById(anyLong())).thenReturn(jobPosting);

        //then
        mockMvc.perform(post("/jobs/" + STRING_TEST_ID)
                .param("action", "apply")
                .param("id", STRING_TEST_ID)
                .param("memberId", STRING_TEST_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/companies/" + jobPosting.getCompany().getId()));

        verify(jobService).applyForJob(member, jobPosting);
    }

    @Test
    @DisplayName("Will show all jobs by company id")
    void getJobsByCompanyId() throws Exception {
        //when
        when(controllerUtils.getCompanyById(anyLong())).thenReturn(company);
        when(jobService.getJobsByCompanyId(anyLong()))
                .thenReturn(List.of(jobPosting));
        //then
        mockMvc.perform(get("/jobs/companies-jobs/" + company.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("jobs"));
    }

    @Test
    @DisplayName("Will redirect to members after job saving")
    void saveJob() throws Exception {
        //when
        when(controllerUtils.getMemberById(anyLong())).thenReturn(member);
        when(controllerUtils.getJobById(anyLong())).thenReturn(jobPosting);

        //then
        mockMvc.perform(post("/jobs/" + STRING_TEST_ID)
                .param("action", "save")
                .param("id", STRING_TEST_ID)
                .param("memberId", STRING_TEST_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(EXPECTED_REDIRECTED_URL + "?tab=sjobs"));

        verify(jobService).saveJob(member, jobPosting);
    }

    private Member createMember() {
        return Member.builder()
                .name(MEMBER_NAME)
                .id(DEFAULT_ID)
                .profile(Profile.builder()
                        .id(DEFAULT_ID)
                        .build())
                .build();
    }

    private Company createCompany() {
        return Company.builder()
                .companySize(COMPANY_SIZE)
                .name(COMPANY_NAME)
                .id(DEFAULT_ID)
                .build();
    }

    private JobPosting createJob() {
        return JobPosting.builder()
                .location(JOB_LOCATION)
                .description(JOB_NAME)
                .company(company)
                .build();
    }
}