package com.ra.course.linkedin.web.controller;

import com.ra.course.linkedin.model.entity.profile.Accomplishment;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.service.accomplishment.AccomplishmentService;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.web.dto.accomplishment.AccomplishmentDto;
import com.ra.course.linkedin.web.mapper.AccomplishmentMapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/accomplishments")
@AllArgsConstructor
public class AccomplishmentController {

    public static final String ADD_ACCOM_TO_PROF =
            "/addNewAccomplishment/{profileId}";
    public static final String PROFILE_ID = "profileId";
    public static final String REDIRECT_PROFILE = "redirect:/profiles/get/";
    public static final String UPDATE_ID = "/update/{id}";
    private final AccomplishmentService accomplishService;
    private final AccomplishmentMapperUtil accomplishMapper;
    private final ProfileService profileService;

    @GetMapping(ADD_ACCOM_TO_PROF)
    public String addAccomplishment(@PathVariable final long profileId,
                                    final Model model) {
        model.addAttribute(PROFILE_ID, profileId);
        return "accomplishment/addAccomplishment";
    }

    @PostMapping(ADD_ACCOM_TO_PROF)
    public String addAccomplishment(@ModelAttribute("accomplishmentForm") final AccomplishmentDto accomplishmentDto) {
        final Accomplishment accomplishment = accomplishMapper
                .mapFromDtoToAccomplishment(accomplishmentDto);
        accomplishService.addOrUpdateAccomplishment(accomplishment);

        return REDIRECT_PROFILE + accomplishmentDto.getProfileId();
    }

    @GetMapping(UPDATE_ID)
    public String getAccomplishment(@PathVariable final long id,
                                    final Model model) {
        final Accomplishment accomplishment =
                accomplishService.getAccomplishmentById(id).get();
        final AccomplishmentDto accomplishmentDto =
                accomplishMapper
                        .mapFromAccomplishmentToDto(accomplishment);
        model.addAttribute("accomplishmentDto",
                accomplishmentDto);

        return "accomplishment/editAccomplishment";
    }

    @PostMapping(UPDATE_ID)
    public String updateAccomplishment(
            final AccomplishmentDto accomplishmentDto) {
        final Accomplishment accomplishment = accomplishMapper
                .mapFromDtoToAccomplishment(accomplishmentDto);
        accomplishService.addOrUpdateAccomplishment(accomplishment);

        return REDIRECT_PROFILE + accomplishment.getProfile().getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteAccomplishment(@PathVariable final long id) {
        final long profileId = accomplishService
                .getAccomplishmentById(id)
                .get()
                .getProfile()
                .getId();
        accomplishService.deleteAccomplishment(
                accomplishService.getAccomplishmentById(id).get());

        return REDIRECT_PROFILE + profileId;
    }

    @GetMapping("/getAccomplishmentsByProfileId/{profileId}")
    public String getAccomplishmentsByProfileId(
            @PathVariable final long profileId, final Model model) {
        final List<AccomplishmentDto> accomplishDtos =
                accomplishService
                        .getAccomplishmentsByProfileID(profileId)
                        .stream().map(
                        accomplishMapper::mapFromAccomplishmentToDto)
                        .collect(Collectors.toList());
        model.addAttribute(
                "accomplishmentDtos", accomplishDtos);
        model.addAttribute(PROFILE_ID, profileId);
        final Optional<Profile> profileOptional =
                profileService.getProfileById(profileId);
        model.addAttribute("memberName",
                profileOptional.isPresent() ?
                        profileOptional.get().getMember().getName() :
                        "NonexistentMember");

        return "accomplishment/accomplishmentsList";
    }
}
