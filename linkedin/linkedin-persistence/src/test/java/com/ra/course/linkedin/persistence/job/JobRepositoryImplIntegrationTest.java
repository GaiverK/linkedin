package com.ra.course.linkedin.persistence.job;

import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.model.entity.other.JobPosting;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.company.CompanyRepository;
import com.ra.course.linkedin.persistence.configuration.RepositoryConfiguration;
import com.ra.course.linkedin.persistence.member.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static com.ra.course.linkedin.persistence.PersistenceIntegrationTestUtils.tryToDelete;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = RepositoryConfiguration.class)
class JobRepositoryImplIntegrationTest {

    private static final String NON_EXISTENT_RECORD_FOR_TEST = "NON_EXISTENT_RECORD_FOR_TEST";
    public static final long NON_EXIST_ID = -1L;
    @Autowired
    JobRepository jobRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    MemberRepository memberRepository;

    private Company company;
    private JobPosting jobPosting;
    private Member member;

    @BeforeEach
    void setUp() {
        member = createAndSaveMember();
        company = createAndSaveCompany();
        jobPosting = createAndSaveJob();
    }

    @AfterEach
    void tearDown() {
        tryToDelete(companyRepository, company);
        tryToDelete(jobRepository, jobPosting);
        tryToDelete(memberRepository, member);
    }

    @Test
    @DisplayName("When searched jobs exist, then must be returned their list")
    void testWhenSearchedJobsExist() {
        //given
        String searchedTitle = "aAa";
        //when
        List<JobPosting> foundJobs = getSearchedJobs(searchedTitle);
        //then
        assertAll(
                () -> assertEquals(foundJobs.size(), 2),
                () -> assertEquals(foundJobs.get(0).getEmploymentType(),
                        createSearchedJobs().get(0).getEmploymentType()),
                () -> assertEquals(foundJobs.get(1).getEmploymentType(),
                        createSearchedJobs().get(2).getEmploymentType())
        );
    }

    @Test
    @DisplayName("When searched jobs do not exist, then must be returned empty list")
    void testWhenSearchedJobsDoNotExist() {
        //given
        String searchedTitle = "bBb";
        //when
        List<JobPosting> foundJobs = getSearchedJobs(searchedTitle);
        //then
        assertEquals(foundJobs.size(), 0);
    }

    @Test
    @DisplayName("When get jobs with specific companyId, " +
            "then return list of jobs with this companyId")
    public void testGetJobsByCompanyId() {
        //when
        List<JobPosting> jobPostings =
                jobRepository.getJobsByCompanyId(company.getId());
        //then
        assertEquals(jobPostings, List.of(jobPosting));

    }

    @Test
    @DisplayName("When get jobs with specific member, " +
            "then return list of jobs with this member")
    public void testGetJobsByMember() {
        //when
        List<JobPosting> jobPostings =
                jobRepository.getJobsByMember(member);
        //then
        assertEquals(jobPostings, List.of(jobPosting));

    }

    private List<JobPosting> createSearchedJobs() {
        List<JobPosting> searchedJobs = new ArrayList<>();

        JobPosting jobPosting = JobPosting.builder()
                .employmentType("aAaA")
                .company(Company.builder().id(NON_EXIST_ID).build())
                .build();
        JobPosting jobPosting1 = JobPosting.builder()
                .employmentType("AbAa")
                .company(Company.builder().id(NON_EXIST_ID).build())
                .build();
        JobPosting jobPosting2 = JobPosting.builder()
                .employmentType("BAaAb")
                .company(Company.builder().id(NON_EXIST_ID).build())
                .build();

        searchedJobs.add(jobPosting);
        searchedJobs.add(jobPosting1);
        searchedJobs.add(jobPosting2);

        return searchedJobs;
    }

    private List<JobPosting> getSearchedJobs(String searchedTitle) {
        List<JobPosting> searchedJobs = createSearchedJobs();
        searchedJobs.forEach(jobRepository::save);
        return jobRepository.searchJobsByEmploymentType(searchedTitle);
    }

    private Company createAndSaveCompany() {
        company = new Company();
        return companyRepository.save(company);
    }

    private Member createAndSaveMember() {
        member = new Member();
        return memberRepository.save(member);
    }

    private JobPosting createAndSaveJob() {
        jobPosting = new JobPosting();
        jobPosting.getMembers().add(member);
        jobPosting.setCompany(company);
        jobPosting.setEmploymentType(NON_EXISTENT_RECORD_FOR_TEST);
        jobRepository.save(jobPosting);

        return jobPosting;
    }
}