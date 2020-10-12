package com.ra.course.linkedin.web.controller;

import com.ra.course.linkedin.model.entity.profile.Experience;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.service.experience.ExperienceService;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.web.dto.experience.ExperienceDto;
import com.ra.course.linkedin.web.mapper.ExperienceMapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/experiences")
@AllArgsConstructor
public class ExperienceController {

    public static final String PROFILE_ID = "profileId";
    public static final String REDIRECT_PROFILE = "redirect:/profiles/get/";
    public static final String UPDATE_ID = "/update/{id}";
    private final ExperienceService experienceService;
    private final ExperienceMapperUtil experienceMapper;
    private final ProfileService profileService;

    @RequestMapping(value = "/addNewExperience/{id}",
            method = RequestMethod.GET)
    public String addExperience(@PathVariable final long id,
                                final Model model) {
        model.addAttribute(PROFILE_ID, id);
        return "experience/addExperience";
    }

    @RequestMapping(value = "/addNewExperience/{profileId}",
            method = RequestMethod.POST)
    public String addExperience(@ModelAttribute("experienceForm") final ExperienceDto experienceDto) {
        final Experience experience =
                experienceMapper.mapFromDtoToExperience(experienceDto);
        experienceService.addOrUpdateExperience(experience);

        return REDIRECT_PROFILE + experienceDto.getProfileId();
    }

    @RequestMapping(value = UPDATE_ID, method = RequestMethod.GET)
    public String getExperience(@PathVariable final long id,
                                final Model model) {
        final Experience experience =
                experienceService.getExperienceById(id).get();
        final ExperienceDto experienceDto =
                experienceMapper.mapFromExperienceToDto(experience);
        model.addAttribute("experience", experienceDto);

        return "experience/editExperience";
    }

    @RequestMapping(value = UPDATE_ID, method = RequestMethod.POST)
    public String updateExperience(final ExperienceDto experienceDto) {

        final Experience experience =
                experienceMapper.mapFromDtoToExperience(experienceDto);
        experienceService.addOrUpdateExperience(experience);

        return REDIRECT_PROFILE + experience.getProfile().getId();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteExperience(@PathVariable final long id) {

        final long profileId = experienceService
                .getExperienceById(id)
                .get()
                .getProfile()
                .getId();
        experienceService.deleteExperience(
                experienceService.getExperienceById(id).get());

        return REDIRECT_PROFILE + profileId;
    }

    @RequestMapping(value = "/getExperiencesByProfileId/{profileId}",
            method = RequestMethod.GET)
    public String getExperiencesByProfileID(
            @PathVariable final long profileId, final Model model) {
        final List<ExperienceDto> experienceDtoList = experienceService
                .getExperiencesByProfileID(profileId)
                .stream()
                .map(experienceMapper::
                        mapFromExperienceToDto)
                .collect(Collectors.toList());
        model.addAttribute("experienceList", experienceDtoList);
        model.addAttribute(PROFILE_ID, profileId);
        final Optional<Profile> optionalProfile =
                profileService.getProfileById(profileId);
        model.addAttribute("memberName",
                optionalProfile.isPresent() ?
                        optionalProfile.get().getMember().getName() :
                        "NonexistentMember");

        return "experience/experiencesList";
    }
}
