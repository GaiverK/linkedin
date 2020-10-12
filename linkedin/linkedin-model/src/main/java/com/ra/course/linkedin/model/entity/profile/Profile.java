package com.ra.course.linkedin.model.entity.profile;

import com.ra.course.linkedin.model.entity.BaseEntity;
import com.ra.course.linkedin.model.entity.person.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Profile extends BaseEntity {

    private String summary;
    private Stats stats;
    private List<Experience> experiences = new ArrayList<>();
    private List<Education> educations = new ArrayList<>();
    private List<Skill> skills = new ArrayList<>();
    private List<Accomplishment> accomplishments = new ArrayList<>();
    private List<Recommendation> recommendations = new ArrayList<>();
    private Member member;

    @Builder
    public Profile(Long id, String summary, Stats stats, List<Experience> experiences, List<Education> educations, List<Skill> skills, List<Accomplishment> accomplishments, List<Recommendation> recommendations, Member member) {
        super(id);
        this.summary = summary;
        this.stats = stats;
        this.experiences = experiences;
        this.educations = educations;
        this.skills = skills;
        this.accomplishments = accomplishments;
        this.recommendations = recommendations;
        this.member = member;
    }
}
