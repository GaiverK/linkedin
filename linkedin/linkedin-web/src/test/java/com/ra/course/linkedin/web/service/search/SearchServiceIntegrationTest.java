package com.ra.course.linkedin.web.service.search;

import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.model.entity.other.JobPosting;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.company.CompanyRepository;
import com.ra.course.linkedin.persistence.job.JobRepository;
import com.ra.course.linkedin.persistence.member.MemberRepository;
import com.ra.course.linkedin.service.search.SearchService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static com.ra.course.linkedin.web.service.ServiceIntegrationTestUtils.tryToDelete;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SearchServiceIntegrationTest {
    public static final String EXIST_SEARCHED_NAME = "aAa";
    public static final String NON_EXIST_SEARCHED_NAME = "NON_EXISTENT_RECORD_FOR_TEST";
    @Autowired
    private SearchService searchService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private JobRepository jobRepository;

    private List<Member> searchedMembers;
    private List<Company> searchedCompanies;
    private List<JobPosting> searchedJobs;

    private List<JobPosting> existingJobs;
    private List<JobPosting> nonExistingJobs;

    private List<Member> existingMembers;
    private List<Member> nonExistingMembers;

    private List<Company> existingCompanies;
    private List<Company> nonExistingCompanies;

    @BeforeEach
    void setUp() {
        searchedMembers = createSearchedMembers();
        searchedCompanies = createSearchedCompanies();
        searchedJobs = createSearchedJobs();

        searchedCompanies.forEach(companyRepository::save);

        existingCompanies = searchService.searchCompaniesByTitle(EXIST_SEARCHED_NAME);
        nonExistingCompanies = searchService.searchCompaniesByTitle(NON_EXIST_SEARCHED_NAME);

        memberRepository.saveMembers(searchedMembers);

        existingMembers = searchService.searchMembersByName(EXIST_SEARCHED_NAME);
        nonExistingMembers = searchService.searchMembersByName(NON_EXIST_SEARCHED_NAME);

        searchedJobs.forEach(jobRepository::save);

        existingJobs = searchService.searchJobsByEmploymentType(EXIST_SEARCHED_NAME);
        nonExistingJobs = searchService.searchJobsByEmploymentType(NON_EXIST_SEARCHED_NAME);
    }

    @AfterEach
    void tearDown() {
        searchedMembers.forEach(member -> tryToDelete(memberRepository, member));
        searchedCompanies.forEach(company -> tryToDelete(companyRepository, company));
        searchedJobs.forEach(jobPosting -> tryToDelete(jobRepository, jobPosting));
    }

    @Test
    @DisplayName("When searched members exist, then must be returned their list")
    void testWhenSearchedMembersExist() {
        //when
        List<Member> foundMembers = existingMembers;
        //then
        assertAll(
                () -> assertEquals(foundMembers.get(0),
                        memberRepository.getById(foundMembers.get(0).getId()).get()),
                () -> assertEquals(foundMembers.get(1),
                        memberRepository.getById(foundMembers.get(1).getId()).get())
        );
    }

    @Test
    @DisplayName("When searched members do not exist, " +
            "then must be returned empty list")
    void testWhenSearchedMembersDoNotExist() {
        //when
        List<Member> foundMembers = nonExistingMembers;
        //then
        assertEquals(foundMembers.size(), 0);
    }

    @Test
    @DisplayName("When searched companies exist, then must be returned their list")
    void testWhenSearchedCompaniesExist() {
        //when
        List<Company> foundCompanies = existingCompanies;
        //then
        assertAll(
                () -> assertEquals(foundCompanies.get(0),
                        companyRepository.getById(foundCompanies.get(0).getId()).get()),
                () -> assertEquals(foundCompanies.get(1),
                        companyRepository.getById(foundCompanies.get(1).getId()).get())
        );
    }

    @Test
    @DisplayName("When searched companies do not exist, " +
            "then must be returned empty list")
    void testWhenSearchedCompaniesDoNotExist() {
        //when
        List<Company> foundCompanies = nonExistingCompanies;
        //then
        assertEquals(foundCompanies.size(), 0);
    }

    @Test
    @DisplayName("When searched jobs exist, then must be returned their list")
    void testWhenSearchedJobsExist() {
        //when
        List<JobPosting> foundJobs = existingJobs;
        //then
        assertAll(
                () -> assertEquals(foundJobs.get(0),
                        jobRepository.getById(foundJobs.get(0).getId()).get()),
                () -> assertEquals(foundJobs.get(1),
                        jobRepository.getById(foundJobs.get(1).getId()).get())
        );
    }

    @Test
    @DisplayName("When searched jobs do not exist, then must be returned empty list")
    void testWhenSearchedJobsDoNotExist() {
        //when
        List<JobPosting> foundJobs = nonExistingJobs;
        //then
        assertEquals(foundJobs.size(), 0);
    }

    private List<Member> createSearchedMembers() {
        List<Member> searchedMembers = new ArrayList<>();

        Member member = Member.builder()
                .name("aAaA")
                .build();
        Member member1 = Member.builder()
                .name("AbAa")
                .build();
        Member member2 = Member.builder()
                .name("BAaAb")
                .build();

        searchedMembers.add(member);
        searchedMembers.add(member1);
        searchedMembers.add(member2);

        return searchedMembers;
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

    private List<JobPosting> createSearchedJobs() {
        List<JobPosting> searchedJobs = new ArrayList<>();

        JobPosting jobPosting = JobPosting.builder()
                .employmentType("aAaA")
                .build();
        JobPosting jobPosting1 = JobPosting.builder()
                .employmentType("AbAa")
                .build();
        JobPosting jobPosting2 = JobPosting.builder()
                .employmentType("BAaAb")
                .build();

        searchedJobs.add(jobPosting);
        searchedJobs.add(jobPosting1);
        searchedJobs.add(jobPosting2);

        return searchedJobs;
    }
}
