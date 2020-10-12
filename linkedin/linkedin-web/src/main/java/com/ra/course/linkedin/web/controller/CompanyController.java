package com.ra.course.linkedin.web.controller;

import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.model.entity.other.JobPosting;
import com.ra.course.linkedin.service.company.CompanyService;
import com.ra.course.linkedin.service.job.JobService;
import com.ra.course.linkedin.web.dto.company.CompanyDto;
import com.ra.course.linkedin.web.dto.job.JobPostingDto;
import com.ra.course.linkedin.web.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    public static final String JOB = "job";
    public static final String UPDATE_COMPANY = "updateCompany";
    public static final String UPDATE_COMPANY_ID = "/update-company/{id}";
    public static final String REDIR_ALL_COMPANS =
            "redirect:/companies/get-all-companies";
    public static final String COMPANY = "company";
    public static final String ADD_NEW_COMPANY = "/add-new-company";
    private final CompanyService companyService;
    private final JobService jobService;
    private final ModelMapper modelMapper;
    private final ControllerUtils controllerUtils;

    @GetMapping(value = ADD_NEW_COMPANY)
    public String addNewCompany(final Model model) {
        model.addAttribute(COMPANY, new CompanyDto());
        return "company/add-new-company";
    }

    @PostMapping(value = ADD_NEW_COMPANY)
    public String addNewCompany(@ModelAttribute(COMPANY) final CompanyDto companyDto) {
        companyService.addOrUpdateCompany(modelMapper.map(companyDto, Company.class));
        return REDIR_ALL_COMPANS;
    }

    @GetMapping(value = "/get-all-companies")
    public String getAllCompanies(final Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "company/companies-list";
    }

    @GetMapping(value = UPDATE_COMPANY_ID)
    public String updateCompany(@PathVariable("id") final Long id,
                                final Model model) {
        model.addAttribute(UPDATE_COMPANY,
                modelMapper.map(controllerUtils.getCompanyById(id), CompanyDto.class));
        return "company/update-company";
    }

    @PostMapping(value = UPDATE_COMPANY_ID)
    public String updateCompany(@PathVariable("id") final Long id,
                                @ModelAttribute(UPDATE_COMPANY) final CompanyDto companyDto) {
        final Company fromDB = controllerUtils.getCompanyById(id);
        final Company toUpdate = modelMapper.map(companyDto, Company.class);
        BeanUtils.copyProperties(toUpdate, fromDB);
        companyService.addOrUpdateCompany(fromDB);
        return REDIR_ALL_COMPANS;
    }

    @GetMapping(value = "/{id}")
    public String getCompanyPage(@PathVariable("id") final Long id,
                                 final Model model) {
        final Company company = controllerUtils.getCompanyById(id);
        company.getPostedJobs().sort((a, b) -> (int) (b.getId() - a.getId()));
        model.addAttribute(COMPANY,
                modelMapper.map(controllerUtils.getCompanyById(id), CompanyDto.class));
        return "company/company-page";
    }

    @PostMapping(value = "/{companyId}/jobs/add-job")
    public String addJob(@PathVariable("companyId") final Long companyId,
                         @ModelAttribute(JOB) final JobPostingDto jobPostingDto) {
        final Company company = controllerUtils.getCompanyById(companyId);
        final JobPosting jobPosting = modelMapper.map(jobPostingDto, JobPosting.class);
        jobPosting.setCompany(company);
        jobService.addJobPosting(jobPosting);
        return "redirect:/companies/" + companyId;
    }
}
