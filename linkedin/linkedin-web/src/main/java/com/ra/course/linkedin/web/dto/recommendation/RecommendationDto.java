package com.ra.course.linkedin.web.dto.recommendation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecommendationDto {
    private long id;
    private String createdOn;
    private String description;
    private long authorId;
    private long authorProfileId;
    private String authorName;

    private long profileId;
    private String memberName;
}
