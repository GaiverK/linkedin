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
public class Group extends BaseEntity {

    private String name;
    private String description;
    private Member owner;
    private List<Member> groupMembers = new ArrayList<>();

    @Builder
    public Group(Long id, String name, String description, Member owner, List<Member> groupMembers) {
        super(id);
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.groupMembers = groupMembers;
    }
}
