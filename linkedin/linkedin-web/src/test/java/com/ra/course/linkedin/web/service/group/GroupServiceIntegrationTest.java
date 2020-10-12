package com.ra.course.linkedin.web.service.group;

import com.ra.course.linkedin.model.entity.exceptions.EntityNotFoundException;
import com.ra.course.linkedin.model.entity.other.Group;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.group.GroupRepository;
import com.ra.course.linkedin.persistence.member.MemberRepository;
import com.ra.course.linkedin.service.group.GroupService;
import com.ra.course.linkedin.service.member.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;

import static com.ra.course.linkedin.web.service.ServiceIntegrationTestUtils.tryToDelete;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GroupServiceIntegrationTest {

    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    private static final long EXIST_ID = 1L;
    private static final long NON_EXIST_ID = -1L;

    private Group group;
    private Member member;

    @BeforeEach
    void setUp() {
        member = createMember();
        memberService.save(member);
        group = createGroup();
        groupService.addGroupToRepo(group);
    }

    @AfterEach
    void tearDown() {
        tryToDelete(groupRepository, group);
        tryToDelete(memberRepository, member);
    }

    @Test
    @DisplayName("When add Group to the repository, it must be saved")
    void testAddGroupToRepo() {
        //then
        assertEquals(groupRepository.getById(group.getId()).get(), group);
    }

    @Test
    @DisplayName("When member joins existing group, " +
            "then he must be contains in members list of this group")
    void testJoinGroupWhenGroupExists() {
        //when
        groupService.joinGroup(member, group);
        //then
        final Group groupFromRepo = groupRepository.getById(this.group.getId()).get();
        assertAll(
                () -> assertEquals(groupFromRepo.getGroupMembers().size(), 1),
                () -> assertTrue(groupFromRepo.getGroupMembers().contains(member))
        );
    }

    @Test
    @DisplayName("When member joins nonexistent group, then exception throws")
    void testJoinGroupWhenGroupDoesNotExist() {
        //given
        Group nonexistentGroup = createGroup();
        nonexistentGroup.setId(NON_EXIST_ID);
        //then
        EntityNotFoundException thrown =
                assertThrows(EntityNotFoundException.class,
                        () -> groupService.joinGroup(member, nonexistentGroup));
        assertTrue(thrown.getMessage().contains(Group.class.getSimpleName()));
    }

    private Group createGroup() {
        return Group.builder()
                .id(EXIST_ID)
                .owner(member)
                .groupMembers(new LinkedList<>())
                .build();
    }

    private Member createMember() {
        return Member.builder()
                .id(EXIST_ID)
                .build();
    }
}
