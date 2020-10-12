package com.ra.course.linkedin.model.entity.profile;

import com.ra.course.linkedin.model.entity.BaseEntity;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Accomplishment extends BaseEntity {

    private String title;
    private LocalDate date;
    private String description;
    private Profile profile;

    @Builder
    public Accomplishment(Long id, String title, LocalDate date,
                          String description, Profile profile) {
        super(id);
        this.title = title;
        this.date = date;
        this.description = description;
        this.profile = profile;
    }
}
