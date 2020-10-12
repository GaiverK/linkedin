package com.ra.course.linkedin.web.controller.recommendation;

import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.model.entity.profile.Recommendation;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.service.recommendation.RecommendationService;
import com.ra.course.linkedin.web.dto.recommendation.RecommendationDto;
import com.ra.course.linkedin.web.mapper.RecommendationMapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ra.course.linkedin.web.controller.profile.ProfileControllerMockMvcTest.SOME_ID_22;
import static com.ra.course.linkedin.web.controller.profile.ProfileControllerMockMvcTest.SOME_PROFILE_ID;
import static java.util.Optional.ofNullable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RecommendationControllerMockMvcTest {

    public static final long SOME_ID_33 = 33L;
    public static final long SOME_ID_44 = 44L;
    @MockBean
    private RecommendationService recommendationService;
    @MockBean
    private RecommendationMapperUtil recommendationMapper;
    @MockBean
    private ProfileService profileService;

    @Autowired
    private MockMvc mockMvc;

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
    @DisplayName("Expect status is Ok and model has attribute profileId")
    public void testGetAddRecommendation() throws Exception {
        //then
        mockMvc.perform(
                get("/recommendations/addNewRecommendation/" +
                        profile.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("profileId"));
    }

    @Test
    @DisplayName("Expect status is Redirection, redirected url is" +
            "/profiles/get/ + profile.getId()" +
            " and recommendationService used method addOrUpdateRecommendation" +
            " with parameter recommendation")
    public void testPostAddRecommendation() throws Exception {
        //when
        when(recommendationMapper.mapFromDtoToRecommendation(any()))
                .thenReturn(recommendation);
        //then
        mockMvc.perform(
                post("/recommendations/addNewRecommendation/" +
                        profile.getId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profiles/get/" + profile.getId()));

        verify(recommendationService).addOrUpdateRecommendation(recommendation);
    }

    @Test
    @DisplayName("Expect status is Ok and model has attribute recommendationDto")
    public void testGetRecommendation() throws Exception {
        //when
        when(recommendationService
                .getRecommendationById(recommendation.getId()))
                .thenReturn(ofNullable(recommendation));
        when(recommendationMapper
                .mapFromRecommendationToDto(recommendation))
                .thenReturn(recommendationDto);
        //then
        mockMvc.perform(
                get("/recommendations/update/" +
                        recommendation.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model()
                        .attributeExists("recommendationDto"));
    }

    @Test
    @DisplayName("Expect status is Redirection, redirected url is" +
            "/profiles/get/ + profile.getId()" +
            "and recommendationService used method addOrUpdateRecommendation " +
            "with parameter recommendation")
    public void testUpdateRecommendation() throws Exception {
        //when
        when(recommendationMapper
                .mapFromDtoToRecommendation(any()))
                .thenReturn(recommendation);
        //then
        mockMvc.perform(
                post("/recommendations/update/" +
                        recommendation.getId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profiles/get/" +
                        recommendation.getProfile().getId()));

        verify(recommendationService).addOrUpdateRecommendation(recommendation);
    }

    @Test
    @DisplayName("Expect status is Redirection, redirected url is" +
            "/profiles/get/ + " +
            "recommendation.getProfile().getId() " +
            "and recommendationService used method deleteRecommendation " +
            "with parameter recommendation")
    public void testDeleteRecommendation() throws Exception {
        //when
        when(recommendationService.getRecommendationById(recommendation.getId()))
                .thenReturn(ofNullable(recommendation));
        //then
        mockMvc.perform(
                get("/recommendations/delete/" +
                        recommendation.getId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profiles/get/" +
                        recommendation.getProfile().getId()));

        verify(recommendationService).deleteRecommendation(recommendation);
    }

    @Test
    @DisplayName("Expect status is Ok and model has attributes: " +
            "recommendationDtoList, profileId, memberName")
    public void testGetRecommendationsByProfileIdWhenMemberExists()
            throws Exception {
        //when
        when(recommendationService
                .getAllRecommendationsByProfileId(profile.getId()))
                .thenReturn(List.of(recommendation));
        when(recommendationMapper.mapFromRecommendationToDto(recommendation))
                .thenReturn(recommendationDto);
        when(profileService.getProfileById(profile.getId()))
                .thenReturn(ofNullable(profile));
        //then
        mockMvc.perform(get
                ("/recommendations/getRecommendationsByProfileId/"
                        + profile.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists
                        ("recommendationDtoList"))
                .andExpect(model().attributeExists("profileId"))
                .andExpect(model().attributeExists("memberName"));
    }

    @Test
    @DisplayName("Expect status is Ok and model has attributes: " +
            "recommendationDtoList, profileId " +
            "and memberName is NonexistentMember")
    public void testGetRecommendationsByProfileIdWhenMemberDoesNotExist()
            throws Exception {
        //when
        when(recommendationService
                .getAllRecommendationsByProfileId(profile.getId()))
                .thenReturn(List.of(recommendation));
        when(recommendationMapper.mapFromRecommendationToDto(recommendation))
                .thenReturn(recommendationDto);
        when(profileService.getProfileById(profile.getId()))
                .thenReturn(Optional.empty());
        //then
        mockMvc.perform(get
                ("/recommendations/getRecommendationsByProfileId/"
                        + profile.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists
                        ("recommendationDtoList"))
                .andExpect(model().attributeExists("profileId"))
                .andExpect(model().attribute("memberName",
                        "NonexistentMember"));
    }

    private Profile createProfile() {
        Member pushkin = Member.builder()
                .name("Pushkin")
                .id(SOME_ID_44)
                .build();

        return Profile.builder()
                .id(SOME_PROFILE_ID)
                .member(pushkin)
                .recommendations(new ArrayList<>())
                .build();
    }

    private Recommendation createRecommendation() {

        Member author = Member.builder()
                .id(SOME_ID_33)
                .name("authorName")
                .build();

        recommendation = new Recommendation();
        recommendation.setProfile(profile);
        recommendation.setId(SOME_ID_22);
        recommendation.setCreatedOn(LocalDate.of(
                2000, 1, 1));
        recommendation.setDescription("111");
        recommendation.setAuthor(author);

        return recommendation;
    }

    private RecommendationDto createRecommendationDto() {
        recommendationDto = new RecommendationDto();
        recommendationDto.setProfileId(profile.getId());
        recommendationDto.setId(SOME_ID_22);
        recommendationDto.setCreatedOn("01-01-2000");
        recommendationDto.setDescription("111");
        recommendationDto.setAuthorName(recommendation.getAuthor().getName());
        recommendationDto.setAuthorId(recommendation.getAuthor().getId());
        recommendationDto.setMemberName(profile.getMember().getName());

        return recommendationDto;
    }
}
