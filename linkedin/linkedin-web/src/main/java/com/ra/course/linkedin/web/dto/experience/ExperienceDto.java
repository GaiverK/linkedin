package com.ra.course.linkedin.web.dto.experience;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExperienceDto {
    private long id;
    private String title;
    private String company;
    private String location;
    private String from;
    private String to;
    private String description;

    private long profileId;
    private String memberName;
}
