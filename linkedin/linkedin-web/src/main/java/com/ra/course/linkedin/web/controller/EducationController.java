package com.ra.course.linkedin.web.controller;

import com.ra.course.linkedin.model.entity.profile.Education;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.service.education.EducationService;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.web.dto.education.EducationDto;
import com.ra.course.linkedin.web.mapper.EducationMapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/educations")
@AllArgsConstructor
public class EducationController {

    public static final String ADD_EDUC_TO_PROF =
            "/addNewEducation/{profileId}";
    public static final String PROFILE_ID = "profileId";
    public static final String REDIRECT_PROFILE = "redirect:/profiles/get/";
    public static final String UPDATE_ID = "/update/{id}";
    private final EducationService educationService;
    private final EducationMapperUtil educationMapper;
    private final ProfileService profileService;

    @GetMapping(ADD_EDUC_TO_PROF)
    public String addEducation(@PathVariable final long profileId,
                               final Model model) {
        model.addAttribute(PROFILE_ID, profileId);

        return "education/addEducation";
    }

    @PostMapping(ADD_EDUC_TO_PROF)
    public String addEducation(@ModelAttribute("educationForm") final EducationDto educationDto) {
        final Education education =
                educationMapper.mapFromDtoToEducation(educationDto);
        educationService.addOrUpdateEducation(education);

        return REDIRECT_PROFILE + educationDto.getProfileId();
    }

    @GetMapping(UPDATE_ID)
    public String getEducation(@PathVariable final long id,
                               final Model model) {
        final Education education =
                educationService.getEducationById(id).get();
        final EducationDto educationDto =
                educationMapper.mapFromEducationToDto(education);
        model.addAttribute("educationDto", educationDto);

        return "education/editEducation";
    }

    @PostMapping(UPDATE_ID)
    public String updateEducation(final EducationDto educationDto) {
        final Education education =
                educationMapper.mapFromDtoToEducation(educationDto);
        educationService.addOrUpdateEducation(education);

        return REDIRECT_PROFILE + education.getProfile().getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteEducation(@PathVariable final long id) {
        final long profileId = educationService
                .getEducationById(id)
                .get()
                .getProfile()
                .getId();
        educationService.deleteEducation(
                educationService.getEducationById(id).get());

        return REDIRECT_PROFILE + profileId;
    }

    @GetMapping("/getEducationsByProfileId/{profileId}")
    public String getEducationsByProfileId(
            @PathVariable final long profileId, final Model model) {
        final List<EducationDto> educationDtoList = educationService
                .getEducationsByProfileID(profileId)
                .stream()
                .map(educationMapper::
                        mapFromEducationToDto)
                .collect(Collectors.toList());
        model.addAttribute("educationDtoList", educationDtoList);
        model.addAttribute(PROFILE_ID, profileId);

        final Optional<Profile> optionalProfile =
                profileService.getProfileById(profileId);
        model.addAttribute("memberName",
                optionalProfile.isPresent() ?
                        optionalProfile.get().getMember().getName() :
                        "NonexistentMember");

        return "education/educationsList";
    }
}
