package com.ra.course.linkedin.web.controller.search;

import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.model.entity.other.JobPosting;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.service.search.SearchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SearchControllerMockMvcTest {

    public static final String SEARCH_INDX = "/search";
    public static final String SEARCH_KEYWORD = "Vasya";
    public static final String MEMBER_NAME = "Vasya";
    public static final String COMPANY_TITLE = "OOO Vasya";
    public static final String EMPLOYMENT_TYPE = "Vasya postings";

    @MockBean
    private SearchService searchService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("will return status 200")
    void main() throws Exception {
        mockMvc.perform(get(SEARCH_INDX))
                .andExpect(status().isOk());
    }

    @Test
    void findResult() throws Exception {
        //given

        //when
        when(searchService.searchMembersByName(anyString())).thenReturn(List.of(getMember()));
        when(searchService.searchCompaniesByTitle(anyString())).thenReturn(List.of(getCompany()));
        when(searchService.searchJobsByEmploymentType(anyString())).thenReturn(List.of(getPosting()));

        //then
        mockMvc.perform(post("/search/all")
                .param("name", SEARCH_KEYWORD)
        )
                .andDo(print())
                .andExpect(model().attributeExists("jobpostings"))
                .andExpect(model().attributeExists("companies"))
                .andExpect(model().attributeExists("members"))
                .andExpect(status().isOk());
    }

    private Member getMember() {
        return Member.builder()
                .name(MEMBER_NAME)
                .build();
    }

    private Company getCompany() {
        return Company.builder()
                .name(COMPANY_TITLE)
                .build();
    }

    private JobPosting getPosting() {
        return JobPosting.builder()
                .company(getCompany())
                .employmentType(EMPLOYMENT_TYPE)
                .isFulfilled(true)
                .build();
    }
}