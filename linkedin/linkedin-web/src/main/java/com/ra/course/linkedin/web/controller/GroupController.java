package com.ra.course.linkedin.web.controller;

import com.ra.course.linkedin.model.entity.other.Group;
import com.ra.course.linkedin.service.group.GroupService;
import com.ra.course.linkedin.web.dto.group.GroupDto;
import com.ra.course.linkedin.web.dto.member.MemberPageDto;
import com.ra.course.linkedin.web.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController {

    public static final String GROUP = "group";
    public static final String CREATE_GROUP = "/create-group";
    public static final long SESSION_ID = 1L;
    public static final String REDIR_GR_SINGLE = "redirect:/groups/single/";
    private final GroupService groupService;
    private final ModelMapper modelMapper;
    private final ControllerUtils controllerUtils;

    @GetMapping(value = CREATE_GROUP)
    public String createGroup(final Model model) {
        final MemberPageDto member = modelMapper.map(controllerUtils.getMemberById(SESSION_ID), MemberPageDto.class);
        model.addAttribute("member", member);
        return "/groups/form";
    }

    @PostMapping(value = CREATE_GROUP)
    public String createGroup(@ModelAttribute(GROUP) final GroupDto groupDto) {
        final Group group = modelMapper.map(groupDto, Group.class);
        groupService.addGroupToRepo(group);
        return REDIR_GR_SINGLE + group.getId();
    }

    @PostMapping(value = "/join-group")
    public String joinGroup(@RequestParam final Long id,
                            @RequestParam("groupId") final Long groupId) {
        groupService.joinGroup(controllerUtils.getMemberById(id), controllerUtils.getGroupById(groupId));
        return REDIR_GR_SINGLE + groupId;
    }

    @GetMapping
    public String allGroups(final Model model) {
        final List<GroupDto> groupDtoList = groupService.getGroups()
                .stream()
                .map(group -> modelMapper.map(group, GroupDto.class))
                .collect(Collectors.toList());
        model.addAttribute("groups", groupDtoList);
        return "groups/index";
    }

    @GetMapping("/single/{id}")
    public String getGroup(@PathVariable final Long id, final Model model) {
        final Optional<Group> group = groupService.getGroupById(id);
        final GroupDto groupDto = modelMapper.map(group.get(), GroupDto.class);
        model.addAttribute(GROUP, groupDto);
        return "groups/groupPage";
    }
}
