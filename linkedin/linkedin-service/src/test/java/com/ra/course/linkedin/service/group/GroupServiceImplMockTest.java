package com.ra.course.linkedin.service.group;

import com.ra.course.linkedin.model.entity.exceptions.EntityNotFoundException;
import com.ra.course.linkedin.model.entity.other.Group;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.group.GroupRepository;
import com.ra.course.linkedin.service.member.MemberService;
import com.ra.course.linkedin.service.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class GroupServiceImplMockTest {
    private GroupService groupService;
    private final GroupRepository mockGroupRepository = mock(GroupRepository.class);
    private final MemberService mockMemberService = mock(MemberService.class);
    private final Utils mockUtils = mock(Utils.class);

    private static final long EXIST_ID = 1L;
    private static final long NON_EXIST_ID = -1L;

    private Group group;

    @BeforeEach
    public void before() {
        groupService = new GroupServiceImpl(
                mockGroupRepository,
                mockMemberService,
                mockUtils);
        group = createGroup(EXIST_ID);
    }

    @Test
    @DisplayName("When all groups gets, then should be called GroupRepository's getAll() method")
    public void testGetAllGroups() {
        //when
        groupService.getGroups();
        //then
        verify(mockGroupRepository).getAll();
    }

    @Test
    @DisplayName("When group created, then called addGroup method")
    public void testAddGroupToRepo() {
        //given
        Member owner = createMember();
        group.setOwner(owner);
        //when
        when(mockUtils.findMember(owner)).thenReturn(owner);
        groupService.addGroupToRepo(group);
        //then
        verify(mockGroupRepository).save(group);
    }

    @Test
    @DisplayName("When member joins group, " +
            "then he must be contains in members list of group")
    public void testJoinGroupWhenGroupExists() {
        //given
        Member member = createMember();
        mockGroupRepository.save(group);
        //when
        when(mockGroupRepository.getById(EXIST_ID)).thenReturn(Optional.of(group));
        when(mockUtils.findMember(member)).thenReturn(member);
        groupService.joinGroup(member, group);
        groupService.joinGroup(member, group);
        //then
        assertTrue(mockGroupRepository.getById(group.getId()).get()
                .getGroupMembers().contains(member));
    }

    @Test
    @DisplayName("When join group which not exist in repository, then exception throw")
    public void testJoinGroupWhenGroupNotExist() {
        //given
        Group nonExistentGroup = createGroup(NON_EXIST_ID);
        //then
        assertThrows(EntityNotFoundException.class,
                () -> groupService.joinGroup(new Member(), nonExistentGroup));
    }

    @Test
    @DisplayName("When delete group, then should be called GroupRepository's deleteGroup() method")
    void testDeleteGroup() {
        //when
        groupService.deleteGroup(group);
        //then
        verify(mockGroupRepository).delete(group);
    }

    private Group createGroup(long id) {
        return Group.builder()
                .id(id)
                .groupMembers(new LinkedList<>())
                .build();
    }

    private Member createMember() {
        return Member.builder()
                .id(EXIST_ID)
                .build();
    }
}
