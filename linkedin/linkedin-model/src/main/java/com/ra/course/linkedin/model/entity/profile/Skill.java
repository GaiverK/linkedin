package com.ra.course.linkedin.model.entity.profile;

import com.ra.course.linkedin.model.entity.BaseEntity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Skill extends BaseEntity {

    private String name;
    private Profile profile;

    @Builder
    public Skill(Long id, String name, Profile profile) {
        super(id);
        this.name = name;
        this.profile = profile;
    }
}
