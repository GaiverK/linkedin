package com.ra.course.linkedin.service.job;

import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.model.entity.other.JobPosting;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.job.JobRepository;
import com.ra.course.linkedin.service.company.CompanyService;
import com.ra.course.linkedin.service.member.MemberService;
import com.ra.course.linkedin.service.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class JobServiceImpl implements JobService {

    private final MemberService memberService;
    private final CompanyService companyService;
    private final JobRepository jobRepository;
    private final Utils utils;

    @Override
    public void saveJob(final Member member, final JobPosting jobPosting) {
        final Member memberToUpdate = utils.findMember(member);
        if (!memberToUpdate.getSavedJobs().contains(jobPosting)) {
            memberToUpdate.getSavedJobs().add(jobPosting);
            memberService.save(memberToUpdate);
        }
        if (!jobPosting.getMembers().contains(memberToUpdate)) {
            jobPosting.getMembers().add(memberToUpdate);
            jobRepository.save(jobPosting);
        }
    }

    @Override
    public void deleteJobFromSaved(final Member member, final JobPosting jobPosting) {
        final Member memberToUpdate = utils.findMember(member);
        memberToUpdate.getSavedJobs().remove(jobPosting);
        memberService.save(memberToUpdate);
    }

    @Override
    public void applyForJob(final Member member, final JobPosting jobPosting) {
        final Member memberToUpdate = utils.findMember(member);
        if (!memberToUpdate.getAppliedJobPosting().contains(jobPosting)) {
            memberToUpdate.getAppliedJobPosting().add(jobPosting);
            memberService.save(memberToUpdate);
        }
        final Company companyToUpdate = utils.findCompany(jobPosting.getCompany());
        companyToUpdate.getAppliedJobs().add(jobPosting);
        companyService.addOrUpdateCompany(companyToUpdate);

        if (!jobPosting.getAcceptedMembers().contains(memberToUpdate)) {
            jobPosting.getAcceptedMembers().add(memberToUpdate);
            jobRepository.save(jobPosting);
        }
    }

    @Override
    public JobPosting addJobPosting(final JobPosting jobPosting) {
        final Company company = utils.findCompany(jobPosting.getCompany());
        company.getPostedJobs().add(jobPosting);
        companyService.addOrUpdateCompany(company);
        return jobRepository.save(jobPosting);
    }

    @Override
    public List<JobPosting> getAllJobs() {
        return StreamSupport.stream(jobRepository.getAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobPosting> getJobsByCompanyId(final Long id) {
        return jobRepository.getJobsByCompanyId(id);
    }

    @Override
    public List<JobPosting> getJobsByMemberId(final Long id) {
        final Optional<Member> member = memberService.getMemberById(id);
        return member.isPresent() ?
                jobRepository.getJobsByMember(member.get()) :
                new ArrayList<>();
    }

    @Override
    public Optional<JobPosting> getJobById(final Long id) {
        return jobRepository.getById(id);
    }

    @Override
    public void deleteJob(final JobPosting jobPosting) {
        jobRepository.delete(jobPosting);
    }
}
