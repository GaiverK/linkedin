package com.ra.course.linkedin.web.controller;

import com.ra.course.linkedin.model.entity.notification.NotificationMethod;
import com.ra.course.linkedin.model.entity.person.AccountStatus;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.service.member.MemberService;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.web.dto.member.MemberPageDto;
import com.ra.course.linkedin.web.dto.member.MemberRegistrationDto;
import com.ra.course.linkedin.web.dto.member.MemberUpdateDto;
import com.ra.course.linkedin.web.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.modelmapper.convention.MatchingStrategies.LOOSE;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    public static final String REDIRECT_MEMBERS = "redirect:/members/";
    public static final String COMPANY_ID = "companyId";
    public static final String MEMBER_ID = "memberId";
    public static final String UPDATE_MEMBER = "updateMember";
    public static final String UPDATE_MEMBER_ID = "/update-member/{id}";
    public static final String REDIR_ALL_MEMBERS =
            "redirect:/members/get-all-members";
    public static final String MEMBER_FORM = "memberForm";
    public static final String REGIST_NEW_MEMBER = "/register-new-member";

    private final MemberService memberService;
    private final ModelMapper modelMapper;
    private final ControllerUtils controllerUtils;
    private final ProfileService profileService;

    @PostMapping(value = REGIST_NEW_MEMBER)
    public String registerNewMember(@ModelAttribute(MEMBER_FORM) final MemberRegistrationDto memberRegistrDto) {
        modelMapper.getConfiguration().setMatchingStrategy(LOOSE);

        final Member member = modelMapper.map(memberRegistrDto, Member.class);

        member.setNotificationMethods(NotificationMethod.values());

        final Profile profile = profileService.saveProfile(new Profile());
        profile.setMember(member);
        member.setProfile(profile);

        member.getAccount().setStatus(AccountStatus.ACTIVE);
        memberService.save(member);
        return "redirect:/profiles/get/" + profile.getId();
    }

    @GetMapping(value = "/get-all-members")
    public String getAllMembers(final Model model) {
        final List<MemberPageDto> memberDtoList = memberService
                .getAllMembers()
                .stream()
                .map((Member member) -> modelMapper.map(member, MemberPageDto.class))
                .collect(Collectors.toList());

        model.addAttribute("members", memberDtoList);
        return "member/members-list";
    }

    @GetMapping(value = UPDATE_MEMBER_ID)
    public String updateMember(@PathVariable("id") final Long id,
                               final Model model) {
        model.addAttribute(UPDATE_MEMBER,
                modelMapper.map(controllerUtils.getMemberById(id), MemberUpdateDto.class));
        return "member/update-member";
    }

    @PostMapping(value = UPDATE_MEMBER_ID)
    public String updateMember(@PathVariable("id") final Long id,
                               @ModelAttribute(UPDATE_MEMBER) final MemberUpdateDto memberUpdateDto) {
        final Member fromDB = controllerUtils.getMemberById(id);
        final Member toUpdate = modelMapper.map(memberUpdateDto, Member.class);
        BeanUtils.copyProperties(toUpdate, fromDB, "account");
        memberService.save(fromDB);
        return REDIR_ALL_MEMBERS;
    }

    @PostMapping(value = "/{id}/follow-company")
    public String followCompany(@PathVariable("id") final Long id,
                                @RequestParam(value = COMPANY_ID) final Long companyId) {
        memberService.followCompany(controllerUtils.getMemberById(id), controllerUtils.getCompanyById(companyId));
        return REDIRECT_MEMBERS + id;
    }

    @PostMapping(value = "/{id}/unfollow-company")
    public String unFollowCompany(@PathVariable("id") final Long id,
                                  @RequestParam(value = COMPANY_ID) final Long companyId) {
        memberService.unFollowCompany(controllerUtils.getMemberById(id), controllerUtils.getCompanyById(companyId));
        return REDIRECT_MEMBERS + id;
    }

    @PostMapping(value = "/{id}/follow-member")
    public String followMember(@PathVariable("id") final Long id,
                               @RequestParam(value = MEMBER_ID) final Long memberId) {
        memberService.followMember(controllerUtils.getMemberById(id), controllerUtils.getMemberById(memberId));
        return REDIRECT_MEMBERS + id;
    }

    @PostMapping(value = "/{id}/unfollow-member")
    public String unFollowMember(@PathVariable("id") final Long id,
                                 @RequestParam(value = MEMBER_ID) final Long memberId) {
        memberService.unFollowMember(controllerUtils.getMemberById(id), controllerUtils.getMemberById(memberId));
        return REDIRECT_MEMBERS + id;
    }
}
