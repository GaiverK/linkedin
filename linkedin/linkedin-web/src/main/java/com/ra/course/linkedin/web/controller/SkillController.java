package com.ra.course.linkedin.web.controller;

import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.model.entity.profile.Skill;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.service.skill.SkillService;
import com.ra.course.linkedin.web.dto.skill.SkillDto;
import com.ra.course.linkedin.web.mapper.SkillMapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/skills")
@AllArgsConstructor
public class SkillController {

    public static final String ADD_SKILL_TO_PROF = "/addNewSkill/{profileId}";
    public static final String PROFILE_ID = "profileId";
    public static final String UPDATE_ID = "/update/{id}";
    public static final String REDIRECT_PROFILE = "redirect:/profiles/get/";
    private final SkillService skillService;
    private final SkillMapperUtil skillMapperUtil;
    private final ProfileService profileService;

    @GetMapping(ADD_SKILL_TO_PROF)
    public String addSkill(@PathVariable final long profileId,
                           final Model model) {
        model.addAttribute(PROFILE_ID, profileId);

        return "skill/addSkill";
    }

    @PostMapping(ADD_SKILL_TO_PROF)
    public String addSkill(
            @ModelAttribute("skillForm") final SkillDto skillDto) {
        final Skill skill = skillMapperUtil.mapFromDtoToSkill(skillDto);
        skillService.addOrUpdateSkill(skill);

        return
                REDIRECT_PROFILE + skillDto.getProfileId();
    }

    @GetMapping(UPDATE_ID)
    public String getSkill(@PathVariable final long id, final Model model) {
        final Skill skill = skillService.getSkillById(id).get();
        final SkillDto skillDto = skillMapperUtil.mapFromSkillToDto(skill);
        model.addAttribute("skillDto", skillDto);

        return "skill/editSkill";
    }

    @PostMapping(UPDATE_ID)
    public String updateSkill(final SkillDto skillDto) {
        final Skill skill = skillMapperUtil.mapFromDtoToSkill(skillDto);
        skillService.addOrUpdateSkill(skill);

        return REDIRECT_PROFILE + skill.getProfile().getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteSkill(@PathVariable final long id) {
        final long profileId = skillService
                .getSkillById(id)
                .get()
                .getProfile()
                .getId();
        skillService.deleteSkill(skillService.getSkillById(id).get());

        return REDIRECT_PROFILE + profileId;
    }

    @GetMapping("/getSkillsByProfileId/{profileId}")
    public String getSkillsByProfileId(@PathVariable final long profileId,
                                       final Model model) {
        final List<SkillDto> skillDtoList = skillService
                .getSkillsByProfileID(profileId)
                .stream()
                .map(skillMapperUtil::mapFromSkillToDto)
                .collect(Collectors.toList());
        model.addAttribute("skillDtos", skillDtoList);
        model.addAttribute(PROFILE_ID, profileId);

        final Optional<Profile> profileOptional =
                profileService.getProfileById(profileId);
        model.addAttribute("memberName",
                profileOptional.isPresent() ?
                        profileOptional.get().getMember().getName() :
                        "NonexistentMember");

        return "skill/skillsList";
    }
}
