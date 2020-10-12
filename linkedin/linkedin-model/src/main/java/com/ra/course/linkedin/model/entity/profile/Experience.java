package com.ra.course.linkedin.model.entity.profile;

import com.ra.course.linkedin.model.entity.BaseEntity;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Experience extends BaseEntity {

    private String title;
    private String company;
    private String location;
    private LocalDate from;
    private LocalDate to;
    private String description;
    private Profile profile;

    @Builder
    public Experience(Long id, String title, String company, String location,
                      LocalDate from, LocalDate to, String description, Profile profile) {
        super(id);
        this.title = title;
        this.company = company;
        this.location = location;
        this.from = from;
        this.to = to;
        this.description = description;
        this.profile = profile;
    }
}
