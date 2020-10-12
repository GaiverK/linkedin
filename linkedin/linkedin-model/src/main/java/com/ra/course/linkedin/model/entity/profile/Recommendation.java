package com.ra.course.linkedin.model.entity.profile;

import com.ra.course.linkedin.model.entity.BaseEntity;
import com.ra.course.linkedin.model.entity.person.Member;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Recommendation extends BaseEntity {

    private Member author;
    private LocalDate createdOn = LocalDate.now();
    private String description;
    private Profile profile;

    @Builder
    public Recommendation(Long id, Member author,
                          String description, Profile profile) {
        super(id);
        this.author = author;
        this.description = description;
        this.profile = profile;
    }
}
