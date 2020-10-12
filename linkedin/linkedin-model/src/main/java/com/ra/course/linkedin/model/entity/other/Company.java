package com.ra.course.linkedin.model.entity.other;

import com.ra.course.linkedin.model.entity.BaseEntity;
import com.ra.course.linkedin.model.entity.person.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Company extends BaseEntity {

    private String name;
    private String description;
    private String type;
    private int companySize;
    private List<Member> followers = new ArrayList<>();
    private List<JobPosting> postedJobs = new ArrayList<>();
    private List<JobPosting> appliedJobs = new ArrayList<>();

    @Builder
    public Company(Long id, String name, String description, String type, int companySize,
                   List<Member> followers, List<JobPosting> postedJobs, List<JobPosting> appliedJobs) {
        super(id);
        this.name = name;
        this.description = description;
        this.type = type;
        this.companySize = companySize;
        this.followers = followers;
        this.postedJobs = postedJobs;
        this.appliedJobs = appliedJobs;
    }
}
