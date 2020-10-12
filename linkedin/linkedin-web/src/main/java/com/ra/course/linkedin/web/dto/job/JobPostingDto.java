package com.ra.course.linkedin.web.dto.job;

import com.ra.course.linkedin.web.dto.member.ContactDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JobPostingDto {
    private Long id;
    private LocalDate dateOfPosting;
    private String description;
    private String employmentType;
    private String location;
    private boolean isFulfilled;
    private long companyId;
    private String companyName;

    private List<ContactDto> members;
    private List<ContactDto> acceptedMembers;
}
