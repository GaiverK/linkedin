package com.ra.course.linkedin.model.entity.other;

import com.ra.course.linkedin.model.entity.BaseEntity;
import com.ra.course.linkedin.model.entity.person.Member;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JobPosting extends BaseEntity {

    private LocalDate dateOfPosting = LocalDate.now();
    private String description;
    private String employmentType;
    private String location;
    private boolean isFulfilled;
    private Company company;
    private List<Member> members = new ArrayList<>();
    private List<Member> acceptedMembers = new ArrayList<>();

    @Builder
    public JobPosting(Long id, String description,
                      String employmentType, String location, boolean isFulfilled,
                      Company company) {
        super(id);
        this.description = description;
        this.employmentType = employmentType;
        this.location = location;
        this.isFulfilled = isFulfilled;
        this.company = company;
    }
}
