package com.ra.course.linkedin.web.dto.education;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EducationDto {
    private long id;
    private String school;
    private String degree;
    private String fromYear;
    private String toYear;
    private String description;

    private long profileId;
    private String memberName;
}
