package com.ra.course.linkedin.web.controller;

import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.model.entity.profile.Recommendation;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.service.recommendation.RecommendationService;
import com.ra.course.linkedin.web.dto.recommendation.RecommendationDto;
import com.ra.course.linkedin.web.mapper.RecommendationMapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/recommendations")
@AllArgsConstructor
public class RecommendationController {

    public static final String ADD_RECOM_TO_PROF = "/addNewRecommendation/{profileId}";
    public static final String PROFILE_ID = "profileId";
    public static final String REDIRECT_PROFILE = "redirect:/profiles/get/";
    public static final String UPDATE_ID = "/update/{id}";
    private final RecommendationService recommendService;
    private final RecommendationMapperUtil recommendatMapper;
    private final ProfileService profileService;

    @GetMapping(ADD_RECOM_TO_PROF)
    public String addRecommendation(@PathVariable final long profileId,
                                    final Model model) {
        model.addAttribute(PROFILE_ID, profileId);

        return "recommendation/addRecommendation";
    }

    @PostMapping(ADD_RECOM_TO_PROF)
    public String addRecommendation(@ModelAttribute("recommendationForm") final RecommendationDto recommendationDto) {
        final Recommendation recommendation = recommendatMapper
                .mapFromDtoToRecommendation(recommendationDto);
        recommendService.addOrUpdateRecommendation(recommendation);

        return REDIRECT_PROFILE + recommendationDto.getProfileId();
    }

    @GetMapping(UPDATE_ID)
    public String getRecommendation(@PathVariable final long id,
                                    final Model model) {
        final Recommendation recommendation = recommendService
                .getRecommendationById(id).get();
        final RecommendationDto recommendationDto = recommendatMapper
                .mapFromRecommendationToDto(recommendation);
        model.addAttribute("recommendationDto", recommendationDto);

        return "recommendation/editRecommendation";
    }

    @PostMapping(UPDATE_ID)
    public String updateRecommendation(final RecommendationDto recommendationDto) {
        final Recommendation recommendation = recommendatMapper
                .mapFromDtoToRecommendation(recommendationDto);
        recommendService.addOrUpdateRecommendation(recommendation);

        return REDIRECT_PROFILE + recommendation.getProfile().getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteRecommendation(@PathVariable final long id) {
        final long profileId = recommendService
                .getRecommendationById(id)
                .get()
                .getProfile()
                .getId();
        recommendService.deleteRecommendation(recommendService
                .getRecommendationById(id).get());

        return REDIRECT_PROFILE + profileId;
    }

    @GetMapping("/getRecommendationsByProfileId/{profileId}")
    public String getRecommendationsByProfileId(
            @PathVariable final long profileId, final Model model) {
        final List<RecommendationDto>
                recommendatDtos = recommendService
                .getAllRecommendationsByProfileId(
                        profileId)
                .stream()
                .map(recommendatMapper
                        ::mapFromRecommendationToDto)
                .collect(Collectors.toList());
        model.addAttribute("recommendationDtoList",
                recommendatDtos);
        model.addAttribute(PROFILE_ID, profileId);
        final Optional<Profile> optionalProfile =
                profileService.getProfileById(profileId);
        model.addAttribute("memberName",
                optionalProfile.isPresent() ?
                        optionalProfile.get().getMember().getName() :
                        "NonexistentMember");
        return "recommendation/recommendationsList";
    }
}
