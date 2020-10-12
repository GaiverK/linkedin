package com.ra.course.linkedin.service.group;

import com.ra.course.linkedin.model.entity.exceptions.EntityNotFoundException;
import com.ra.course.linkedin.model.entity.other.Group;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.group.GroupRepository;
import com.ra.course.linkedin.service.member.MemberService;
import com.ra.course.linkedin.service.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final MemberService memberService;
    private final Utils utils;

    @Override
    public Group addGroupToRepo(final Group group) {
        final Member owner = utils.findMember(group.getOwner());
        owner.getCreatedGroups().add(group);
        memberService.save(owner);
        return groupRepository.save(group);
    }

    @Override
    public void joinGroup(final Member member, final Group group) {
        final Group joinedGroup = getGroupById(group.getId())
                .orElseThrow(() -> new EntityNotFoundException(Group.class.getSimpleName()));

        if (!joinedGroup.getGroupMembers().contains(member)) {
            joinedGroup.getGroupMembers().add(member);
            groupRepository.save(joinedGroup);
            final Member updatedMember = utils.findMember(member);
            updatedMember.getJoinedGroups().add(group);
            memberService.save(updatedMember);
        }
    }

    @Override
    public Optional<Group> getGroupById(final Long id) {
        return groupRepository.getById(id);
    }

    @Override
    public List<Group> getGroups() {
        return (List<Group>) groupRepository.getAll();
    }

    @Override
    public void deleteGroup(final Group group) {
        groupRepository.delete(group);
    }
}
