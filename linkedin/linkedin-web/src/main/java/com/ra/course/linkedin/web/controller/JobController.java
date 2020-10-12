package com.ra.course.linkedin.web.controller;

import com.ra.course.linkedin.model.entity.other.JobPosting;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.service.job.JobService;
import com.ra.course.linkedin.web.dto.job.JobPostingDto;
import com.ra.course.linkedin.web.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    public static final String REDIRECT_PROF = "redirect:/profiles/get/";
    public static final String REDIRECT_COMP = "redirect:/companies/";
    public static final String MEMBER_ID = "memberId";
    public static final String ID = "/{id}";
    public static final String JOBS = "jobs";
    private final JobService jobService;
    private final ModelMapper modelMapper;
    private final ControllerUtils controllerUtils;

    @GetMapping(value = "/all")
    public String getJobs(final Model model) {
        final List<JobPostingDto> jobPostingDtos =
                jobService
                        .getAllJobs()
                        .stream().map(jobPosting -> modelMapper.map(jobPosting, JobPostingDto.class))
                        .collect(Collectors.toList());
        model.addAttribute(JOBS, jobPostingDtos);
        return "job/jobsList";
    }

    @GetMapping(value = "/companies-jobs/{id}")
    public String getJobsByCompanyId(@PathVariable final Long id, final Model model) {
        final List<JobPostingDto> jobPostingDtos = jobService
                        .getJobsByCompanyId(id)
                        .stream()
                .map(jobPosting -> modelMapper.map(jobPosting, JobPostingDto.class))
                        .collect(Collectors.toList());
        model.addAttribute(JOBS, jobPostingDtos);
        return "job/companies-jobs";
    }

    @PostMapping(value = ID, params = "action=apply")
    public String applyJob(@PathVariable("id") final Long id,
                           @RequestParam(name = MEMBER_ID) final Long memberId) {
        final Member member = controllerUtils.getMemberById(memberId);
        final JobPosting job = controllerUtils.getJobById(id);
        jobService.applyForJob(member, job);
        return REDIRECT_COMP + job.getCompany().getId();
    }

    @PostMapping(value = ID, params = "action=save")
    public String saveJob(@PathVariable("id") final Long id,
                           @RequestParam(name = MEMBER_ID) final Long memberId) {
        final Member member = controllerUtils.getMemberById(memberId);
        jobService.saveJob(member, controllerUtils.getJobById(id));
        return REDIRECT_PROF + member.getProfile().getId() + "?tab=sjobs";
    }
}
