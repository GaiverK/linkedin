package com.ra.course.linkedin.web.controller;

import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.model.entity.other.JobPosting;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.service.search.SearchService;
import com.ra.course.linkedin.web.dto.search.FindCompanyDTO;
import com.ra.course.linkedin.web.dto.search.FindJobPostingDTO;
import com.ra.course.linkedin.web.dto.search.FindMemberDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("search")
@Controller
public class SearchController {

    public static final String SEARCH_INDX_REDIR = "search/index";
    private final transient SearchService searchService;
    private final transient ModelMapper modelMapper;

    public SearchController(final SearchService searchService, final ModelMapper modelMapper) {
        this.searchService = searchService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public String main() {
        return SEARCH_INDX_REDIR;
    }

    @PostMapping("/all")
    public String findResult(@RequestParam(defaultValue = "") final String name, final Model model) {
        final List<Member> members = searchService.searchMembersByName(name);
        final List<FindMemberDTO> memberDTOS = members.stream().map(member -> modelMapper.map(member, FindMemberDTO.class)).collect(Collectors.toList());

        final List<Company> companies = searchService.searchCompaniesByTitle(name);
        final List<FindCompanyDTO> companyDTOS = companies.stream().map(company -> modelMapper.map(company, FindCompanyDTO.class)).collect(Collectors.toList());

        final List<JobPosting> jobPostings = searchService.searchJobsByEmploymentType(name);
        final List<FindJobPostingDTO> jobPostingDTOS = jobPostings.stream().map(jobPosting -> modelMapper.map(jobPosting, FindJobPostingDTO.class)).collect(Collectors.toList());

        model.addAttribute("jobpostings", jobPostingDTOS);
        model.addAttribute("companies", companyDTOS);
        model.addAttribute("members", memberDTOS);
        return SEARCH_INDX_REDIR;
    }
}
