package com.ra.course.linkedin.web.controller.experience;

import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.model.entity.profile.Experience;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.service.experience.ExperienceService;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.web.dto.experience.ExperienceDto;
import com.ra.course.linkedin.web.mapper.ExperienceMapperUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ExperienceControllerMockMvcTest {

    public static final String DEFAULT_S_PROFILE_ID = "1";
    public static final Long DEFAULT_L_PROFILE_ID = 1L;
    public static final String EXPERIENCE_TITLE = "Experience title";
    public static final String MEMBER_NAME = "Vasya";
    public static final String EXPERIENCE_DESCRIPTION = "Experience description";

    @MockBean
    private ExperienceService experienceService;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private ExperienceMapperUtil mapperUtil;

    @Autowired
    private MockMvc mockMvc;

    private final Experience experience = getReadyExperience();
    private final ExperienceDto experienceDto = getExperienceDTO();

    @Test
    @DisplayName("If add experience get request, then status 200 and attr profileId exists")
    void getAddExperience() throws Exception {
        this.mockMvc.perform(get("/experiences/addNewExperience/" + DEFAULT_S_PROFILE_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("profileId"));
    }

    @Test
    @DisplayName("If add experience Post request, then check status 200 and redirected url")
    void postAddExperience() throws Exception {
        when(mapperUtil.mapFromDtoToExperience(any())).thenReturn(experience);

        this.mockMvc.perform(post("/experiences/addNewExperience/" + DEFAULT_S_PROFILE_ID)
                .param("profileId", DEFAULT_S_PROFILE_ID)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profiles/get/" + DEFAULT_S_PROFILE_ID));

        verify(experienceService).addOrUpdateExperience(experience);
    }

    @Test
    @DisplayName("If experience exists then status ok and model has attr accomplishmentDto")
    void getExperience() throws Exception {
        when(experienceService.getExperienceById(experience.getId())).thenReturn(Optional.of(experience));
        when(mapperUtil.mapFromExperienceToDto(any())).thenReturn(experienceDto);

        this.mockMvc.perform(get("/experiences/update/" + experience.getId())
        )
                .andExpect(model().attributeExists("experience"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("After experience update will redirect to all experiences by profile id and verify that service worked")
    void updateExperience() throws Exception {
        when(mapperUtil.mapFromDtoToExperience(any())).thenReturn(experience);

        this.mockMvc.perform(post("/experiences/update/" + experience.getId())
                .param("profileId", DEFAULT_S_PROFILE_ID)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profiles/get/" + DEFAULT_S_PROFILE_ID));

        verify(experienceService).addOrUpdateExperience(experience);
    }

    @Test
    @DisplayName("After experience delete will redirect to all experiences by profile id and verify that service worked")
    void deleteExperience() throws Exception {
        when(experienceService.getExperienceById(experience.getId())).thenReturn(Optional.of(experience));
        this.mockMvc.perform(get("/experiences/delete/" + experience.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profiles/get/" + DEFAULT_S_PROFILE_ID));
        verify(experienceService).deleteExperience(experience);
    }

    @Test
    @DisplayName("Will return list of experiences by profile id and Member name")
    void getExperiencesByProfileID() throws Exception {
        when(experienceService.getExperiencesByProfileID(DEFAULT_L_PROFILE_ID)).thenReturn(List.of(experience));
        when(mapperUtil.mapFromExperienceToDto(experience)).thenReturn(experienceDto);
        when(profileService.getProfileById(DEFAULT_L_PROFILE_ID)).thenReturn(Optional.ofNullable(getProfileWithMember()));

        this.mockMvc.perform(get("/experiences/getExperiencesByProfileId/" + DEFAULT_S_PROFILE_ID)
        )
                .andExpect(model().attributeExists("experienceList"))
                .andExpect(model().attributeExists("profileId"))
                .andExpect(model().attributeExists("memberName"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Will return list of experiences by profile id and NonexistentMember")
    void getExperiencesByProfileIDWhenMemberNotExists() throws Exception {
        when(experienceService.getExperiencesByProfileID(DEFAULT_L_PROFILE_ID)).thenReturn(List.of(experience));
        when(mapperUtil.mapFromExperienceToDto(experience)).thenReturn(experienceDto);
        when(profileService.getProfileById(DEFAULT_L_PROFILE_ID)).thenReturn(getEmptyProfile());

        this.mockMvc.perform(get("/experiences/getExperiencesByProfileId/" + DEFAULT_S_PROFILE_ID)
        )
                .andExpect(model().attributeExists("experienceList"))
                .andExpect(model().attributeExists("profileId"))
                .andExpect(model().attribute("memberName", "NonexistentMember"))
                .andExpect(status().isOk());
    }

    private Profile getProfileWithMember() {

        return Profile.builder()
                .id(DEFAULT_L_PROFILE_ID)
                .member(Member.builder().name(MEMBER_NAME).build())
                .build();
    }

    private Optional<Profile> getEmptyProfile() {
        return Optional.empty();
    }

    private ExperienceDto getExperienceDTO() {
        ExperienceDto dto = new ExperienceDto();
        dto.setProfileId(DEFAULT_L_PROFILE_ID);
        dto.setTitle(EXPERIENCE_TITLE);
        dto.setDescription(EXPERIENCE_DESCRIPTION);
        dto.setMemberName(MEMBER_NAME);
        return dto;
    }

    private Experience getReadyExperience() {
        return Experience.builder()
                .id(DEFAULT_L_PROFILE_ID)
                .title(EXPERIENCE_TITLE)
                .description(EXPERIENCE_DESCRIPTION)
                .profile(Profile.builder().id(DEFAULT_L_PROFILE_ID).build())
                .build();
    }
}