package com.ra.course.linkedin.web.dto.accomplishment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccomplishmentDto {
    private long id;
    private String title;
    private String date;
    private String description;

    private long profileId;
    private String memberName;
}
