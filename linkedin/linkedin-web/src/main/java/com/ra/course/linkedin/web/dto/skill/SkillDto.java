package com.ra.course.linkedin.web.dto.skill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SkillDto {
    private long id;
    private String name;

    private long profileId;
    private String memberName;
}
