package com.ra.course.linkedin.web.controller;

import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.service.member.MemberService;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.web.dto.member.MemberPageDto;
import com.ra.course.linkedin.web.dto.profile.ProfileDto;
import com.ra.course.linkedin.web.mapper.ProfileMapperUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/profiles")
@AllArgsConstructor
public class ProfileController {

    public static final String GET_ID = "/get/{id}";
    private final ProfileService profileService;
    private final MemberService memberService;
    private final ProfileMapperUtil profileMapperUtil;
    private final ModelMapper mapper;

    @RequestMapping(value = GET_ID, method = RequestMethod.GET)
    public String getProfile(@PathVariable final long id, final Model model) {

        final Profile profile = profileService.getProfileById(id).get();
        final ProfileDto profileDto = profileMapperUtil.mapFromProfileToDto(profile);
        final List<MemberPageDto> allMembers = memberService.getAllMembers().stream()
                .map(member -> mapper.map(member, MemberPageDto.class)).collect(Collectors.toList());

        model.addAttribute("profile", profileDto);
        model.addAttribute("allMembers", allMembers);

        return "profile/viewProfile";
    }

    @RequestMapping(value = GET_ID, method = RequestMethod.POST)
    public String updateProfileSummary(final ProfileDto profileDto) {

        final Profile profile = profileMapperUtil.mapFromDtoToProfile(profileDto);
        profileService.saveProfile(profile);

        return "redirect:/profiles/get/" + profile.getId();
    }
}
