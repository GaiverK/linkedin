package com.ra.course.linkedin.web.dto.search;

import com.ra.course.linkedin.model.entity.other.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FindJobPostingDTO {
    private long id;
    private String employmentType;
    private String description;
    private Company company;
    private String location;
    private LocalDate dateOfPosting;
    private Boolean isFulfilled;
}
