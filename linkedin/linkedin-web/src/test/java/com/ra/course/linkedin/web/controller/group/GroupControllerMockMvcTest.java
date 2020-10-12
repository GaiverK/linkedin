package com.ra.course.linkedin.web.controller.group;

import com.ra.course.linkedin.model.entity.other.Group;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.service.group.GroupService;
import com.ra.course.linkedin.web.dto.group.GroupDto;
import com.ra.course.linkedin.web.utils.ControllerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GroupControllerMockMvcTest {

    public static final String MEMBER_NAME = "Pushkin";
    public static final String TEST_DESCRIPTION = "Description";
    public static final String GROUP_NAME = "Group name";
    public static final String TEST_NAME = "Test name";
    public static final long OWNER_ID = 1L;
    public static final String EXPECTED_REDIRECTED_URL = "/members/" + OWNER_ID;
    public static final long DEFAULT_ID = 1L;

    @MockBean
    private GroupService groupService;
    @MockBean
    private ControllerUtils controllerUtils;

    @Autowired
    private MockMvc mockMvc;

    private GroupDto groupDto;
    private Member member;
    private Group group;

    @BeforeEach
    void setUp() {
        member = createMember();
        groupDto = createGroupDTO();
        group = getGroup();
    }

    @Test
    @DisplayName("will return edit page for group")
    void createGroupTest() throws Exception {
        when(controllerUtils.getMemberById(anyLong())).thenReturn(member);
        //then
        mockMvc.perform(get("/groups/create-group"))
                .andExpect(status().isOk())
                .andExpect(view().name("/groups/form"));
    }

    @Test
    @DisplayName("will add group to repo and then redirect to all members")
    void createGroup() throws Exception {

        //then
        mockMvc.perform(post("/groups/create-group")
                .param("id", String.valueOf(groupDto.getOwnerId()))
                .param("name", groupDto.getName())
                .param("description", groupDto.getDescription()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups/single/" + groupDto.getId()));

        verify(groupService).addGroupToRepo(any(Group.class));
    }

    @Test
    @DisplayName("will redirect to all members after joining group")
    void joinGroup() throws Exception {
        //when
        when(controllerUtils.getMemberById(anyLong())).thenReturn(member);
        when(controllerUtils.getGroupById(anyLong())).thenReturn(group);

        //then
        mockMvc.perform(post("/groups/join-group")
                .param("id", String.valueOf(groupDto.getOwnerId()))
                .param("groupId", String.valueOf(groupDto.getOwnerId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups/single/" + group.getId()));

        verify(groupService).joinGroup(member, group);
    }

    @Test
    @DisplayName("will return all groups from repository")
    void allGroups() throws Exception {
        //when
        when(groupService.getGroups()).thenReturn(List.of(group));
        //then
        mockMvc.perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/index"));
    }

    @Test
    @DisplayName("will return group by group id")
    void getGroupTest() throws Exception {
        //when
        when(groupService.getGroupById(anyLong())).thenReturn(ofNullable(group));
        //then
        mockMvc.perform(get("/groups/single/" + group.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/groupPage"));
    }

    private GroupDto createGroupDTO() {
        GroupDto dto = new GroupDto();
        dto.setId(DEFAULT_ID);
        dto.setOwnerId(OWNER_ID);
        dto.setName(TEST_NAME);
        dto.setDescription(TEST_DESCRIPTION);
        return dto;
    }

    private Member createMember() {
        return Member.builder()
                .name(MEMBER_NAME)
                .id(DEFAULT_ID)
                .build();
    }

    private Group getGroup() {
        return Group.builder()
                .description(TEST_DESCRIPTION)
                .name(GROUP_NAME)
                .owner(new Member())
                .groupMembers(new ArrayList<>())
                .id(DEFAULT_ID)
                .build();
    }
}