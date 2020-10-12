package com.ra.course.linkedin.service.search;

import com.ra.course.linkedin.persistence.company.CompanyRepository;
import com.ra.course.linkedin.persistence.job.JobRepository;
import com.ra.course.linkedin.persistence.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SearchServiceImplMockTest {

    private final MemberRepository mockMemberRepository = mock(MemberRepository.class);
    private final CompanyRepository mockCompanyRepository =
            mock(CompanyRepository.class);
    private final JobRepository mockJobRepository = mock(JobRepository.class);

    private SearchService searchService;

    @BeforeEach
    public void before() {
        searchService = new SearchServiceImpl(mockMemberRepository,
                mockCompanyRepository,
                mockJobRepository);
    }

    @Test
    @DisplayName("When search members by name, then must be invoked member " +
            "repository method searchMembersByName")
    void searchMembersByName() {
        //given
        String searchedName = "aAa";
        //when
        searchService.searchMembersByName(searchedName);
        //then
        verify(mockMemberRepository).searchMembersByName(searchedName);
    }

    @Test
    @DisplayName("When search companies by name, then must be invoked company " +
            "repository method searchCompaniesByTitle")
    void searchCompaniesByTitle() {
        //given
        String searchedTitle = "aAa";
        //when
        searchService.searchCompaniesByTitle(searchedTitle);
        //then
        verify(mockCompanyRepository).searchCompaniesByTitle(searchedTitle);
    }

    @Test
    @DisplayName("When search jobs by name, then must be invoked job " +
            "repository method searchJobsByEmploymentType")
    void searchJobsByEmploymentType() {
        //given
        String searchedEmploymentType = "aAa";
        //when
        searchService.searchJobsByEmploymentType(searchedEmploymentType);
        //then
        verify(mockJobRepository).searchJobsByEmploymentType(searchedEmploymentType);
    }
}