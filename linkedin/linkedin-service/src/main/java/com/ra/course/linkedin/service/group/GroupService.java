package com.ra.course.linkedin.service.group;

import com.ra.course.linkedin.model.entity.other.Group;
import com.ra.course.linkedin.model.entity.person.Member;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    Group addGroupToRepo(Group group);

    void joinGroup(Member member, Group group);

    Optional<Group> getGroupById(Long id);

    List<Group> getGroups();

    void deleteGroup(Group group);
}
