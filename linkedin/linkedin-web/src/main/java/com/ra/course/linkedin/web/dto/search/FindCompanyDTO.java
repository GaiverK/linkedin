package com.ra.course.linkedin.web.dto.search;

import com.ra.course.linkedin.web.dto.job.JobPostingDto;
import com.ra.course.linkedin.web.dto.member.ContactDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FindCompanyDTO {
    private Long id;
    private String name;
    private String description;
    private String type;
    private int companySize;
    private List<ContactDto> followers = new ArrayList<>();
    private List<JobPostingDto> postedJobs = new ArrayList<>();
    private List<JobPostingDto> appliedJobs = new ArrayList<>();
}
