package com.ra.course.linkedin.web.controller;

import com.ra.course.linkedin.model.entity.person.Admin;
import com.ra.course.linkedin.service.admin.AdminService;
import com.ra.course.linkedin.service.member.MemberService;
import com.ra.course.linkedin.web.dto.member.ContactDto;
import com.ra.course.linkedin.web.dto.member.MemberPageDto;
import com.ra.course.linkedin.web.dto.member.MemberRegistrationDto;
import com.ra.course.linkedin.web.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.modelmapper.convention.MatchingStrategies.LOOSE;

@Controller
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    public static final String REDIRECT_ADMINS = "redirect:/admins/";
    public static final String MEMBER_ID = "memberId";
    public static final String ADMIN_FORM = "adminForm";
    public static final String ADD_NEW_ADMIN = "/register";
    public static final String ADMIN_LOGIN = "admin/login";
    private final AdminService adminService;
    private final MemberService memberService;
    private final ModelMapper modelMapper;
    private final ControllerUtils controllerUtils;

    @GetMapping(value = ADD_NEW_ADMIN)
    public String addNewAdmin(final Model model) {
        model.addAttribute(ADMIN_FORM, new MemberRegistrationDto());
        model.addAttribute("operation", "register");
        return ADMIN_LOGIN;
    }

    @PostMapping(value = ADD_NEW_ADMIN)
    public String addNewAdmin(@ModelAttribute final MemberRegistrationDto adminRegistratDto) {
        modelMapper.getConfiguration().setMatchingStrategy(LOOSE);
        final Admin admin = modelMapper.map(adminRegistratDto, Admin.class);
        adminService.save(admin);
        return REDIRECT_ADMINS + admin.getId();
    }

    @GetMapping
    public String login() {
        return ADMIN_LOGIN;
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") final MemberRegistrationDto memberRegistrDto) {
        final Optional<Admin> admin = adminService.login(memberRegistrDto.getEmail(), memberRegistrDto.getPassword());
        return admin.map(user -> REDIRECT_ADMINS + user.getId()).orElse("redirect:/admins");
    }

    @GetMapping(value = "/get-all-admins")
    public String getAllAdmins(final Model model) {
        model.addAttribute("admins", adminService.getAllAdmins());
        return "admin/admins-list";
    }

    @GetMapping(value = "{id}")
    public String getAdminPage(@PathVariable("id") final Long id,
                               final Model model) {
        final List<MemberPageDto> allMembers = memberService.getAllMembers()
                .stream()
                .map(member -> modelMapper.map(member, MemberPageDto.class))
                .collect(Collectors.toList());
        model.addAttribute("admin", modelMapper.map(controllerUtils.getAdminById(id), ContactDto.class));

        model.addAttribute("members", allMembers);
        return "admin/admin-page";
    }

    @PostMapping(value = "/{id}/block-member")
    public String blockMember(@PathVariable("id") final Long id,
                              @RequestParam(value = MEMBER_ID) final Long memberId) {
        adminService.blockMember(controllerUtils.getMemberById(memberId));
        return REDIRECT_ADMINS + id;
    }

    @PostMapping(value = "/{id}/unblock-member")
    public String unblockMember(@PathVariable("id") final Long id,
                                @RequestParam(value = MEMBER_ID) final Long memberId) {
        adminService.unblockMember(controllerUtils.getMemberById(memberId));
        return REDIRECT_ADMINS + id;
    }
}
