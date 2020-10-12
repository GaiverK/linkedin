package com.ra.course.linkedin.model.entity.profile;

import com.ra.course.linkedin.model.entity.BaseEntity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Education extends BaseEntity {

    private String school;
    private String degree;
    private String fromYear;
    private String toYear;
    private String description;
    private Profile profile;

    @Builder
    public Education(Long id, String school, String degree, String fromYear,
                     String toYear, String description, Profile profile) {
        super(id);
        this.school = school;
        this.degree = degree;
        this.fromYear = fromYear;
        this.toYear = toYear;
        this.description = description;
        this.profile = profile;
    }
}
