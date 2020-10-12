package com.ra.course.linkedin.web.mapper.recommendation;

import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.model.entity.profile.Recommendation;
import com.ra.course.linkedin.service.member.MemberService;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.web.dto.recommendation.RecommendationDto;
import com.ra.course.linkedin.web.mapper.RecommendationMapperUtil;
import com.ra.course.linkedin.web.utils.ConverterUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static com.ra.course.linkedin.web.controller.profile.ProfileControllerMockMvcTest.SOME_ID_22;
import static com.ra.course.linkedin.web.controller.profile.ProfileControllerMockMvcTest.SOME_PROFILE_ID;
import static com.ra.course.linkedin.web.controller.recommendation.RecommendationControllerMockMvcTest.SOME_ID_33;
import static com.ra.course.linkedin.web.controller.recommendation.RecommendationControllerMockMvcTest.SOME_ID_44;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RecommendationMapperUtilTest {

    @MockBean
    private ProfileService profileService;
    @MockBean
    private MemberService memberService;
    @Autowired
    private ConverterUtils util;
    @Autowired
    private RecommendationMapperUtil recommendationMapper;

    private Profile profile;
    private Recommendation recommendation;
    private RecommendationDto recommendationDto;

    @BeforeEach
    void setUp() {
        profile = createProfile();
        recommendation = createRecommendation();
        recommendationDto = createRecommendationDto();
    }

    @Test
    @DisplayName("Expect recommendationDto is mapped properly")
    public void testMapFromRecommendationToDto() {
        //when
        RecommendationDto result = recommendationMapper
                .mapFromRecommendationToDto(recommendation);
        //then
        assertEquals(recommendationDto, result);
    }

    @Test
    @DisplayName("Expect recommendation has correct parameters ")
    public void testMapFromDtoToRecommendation() {
        //when
        when(profileService.getProfileById(recommendationDto.getProfileId()))
                .thenReturn(ofNullable(profile));
        when(memberService.getMemberById(recommendationDto.getAuthorId()))
                .thenReturn(ofNullable(recommendation.getAuthor()));
        Recommendation result = recommendationMapper
                .mapFromDtoToRecommendation(recommendationDto);
        //then
        assertAll(
                () -> assertEquals(recommendation.getProfile(),
                        result.getProfile()),
                () -> assertEquals(recommendation.getId(), result.getId()),
                () -> assertEquals(recommendation.getDescription(),
                        result.getDescription()),
                () -> assertEquals(recommendation.getAuthor(),
                        result.getAuthor()));
    }

    @Test
    @DisplayName("Expect recommendation has correct parameters ")
    public void testMapFromDtoToRecommendationWhenCreatedOnIsNull() {
        //when
        recommendationDto.setCreatedOn(null);
        when(profileService.getProfileById(recommendationDto.getProfileId()))
                .thenReturn(ofNullable(profile));
        when(memberService.getMemberById(recommendationDto.getAuthorId()))
                .thenReturn(ofNullable(recommendation.getAuthor()));
        Recommendation result = recommendationMapper
                .mapFromDtoToRecommendation(recommendationDto);
        //then
        assertAll(
                () -> assertEquals(recommendation.getProfile(),
                        result.getProfile()),
                () -> assertEquals(recommendation.getId(), result.getId()),
                () -> assertEquals(recommendation.getDescription(),
                        result.getDescription()),
                () -> assertEquals(recommendation.getAuthor(),
                        result.getAuthor()));
    }

    private Profile createProfile() {
        Member pushkin = Member.builder()
                .name("Pushkin")
                .id(SOME_ID_44)
                .build();

        return Profile.builder()
                .id(SOME_PROFILE_ID)
                .member(pushkin)
                .build();
    }

    private Recommendation createRecommendation() {

        Member author = Member.builder()
                .id(SOME_ID_33)
                .name("authorName")
                .build();

        return Recommendation.builder()
                .profile(profile)
                .id(SOME_ID_22)
                .description("Description")
                .author(author)
                .build();
    }

    private RecommendationDto createRecommendationDto() {
        recommendationDto = new RecommendationDto();
        recommendationDto.setProfileId(profile.getId());
        recommendationDto.setId(SOME_ID_22);
        recommendationDto.setCreatedOn(util.formattedDate(LocalDate.now()));
        recommendationDto.setDescription("Description");
        recommendationDto.setAuthorName(recommendation.getAuthor().getName());
        recommendationDto.setAuthorId(recommendation.getAuthor().getId());
        recommendationDto.setMemberName(profile.getMember().getName());

        return recommendationDto;
    }
}
