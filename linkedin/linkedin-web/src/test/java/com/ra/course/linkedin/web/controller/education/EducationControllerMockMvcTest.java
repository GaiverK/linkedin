package com.ra.course.linkedin.web.controller.education;

import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.model.entity.profile.Education;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.service.education.EducationService;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.web.dto.education.EducationDto;
import com.ra.course.linkedin.web.mapper.EducationMapperUtil;
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
class EducationControllerMockMvcTest {

    public static final String DEFAULT_S_PROFILE_ID = "1";
    public static final Long DEFAULT_L_PROFILE_ID = 1L;
    public static final String MEMBER_NAME = "Vasya";
    public static final String EDUCATION_DESCRIPTION = "Education description";

    @MockBean
    private EducationService educationService;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private EducationMapperUtil mapperUtil;

    @Autowired
    private MockMvc mockMvc;

    private final Education education = getReadyEducation();
    private final EducationDto educationDto = getEducationDTO();

    @Test
    @DisplayName("If add education get request, then status 200 and attr profileId exists")
    void getAddEducation() throws Exception {
        this.mockMvc.perform(get("/educations/addNewEducation/" + DEFAULT_S_PROFILE_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("profileId"));
    }

    @Test
    @DisplayName("If add accomplishment Post request, then check status 200 and redirected url")
    void postAddEducation() throws Exception {
        when(mapperUtil.mapFromDtoToEducation(any())).thenReturn(education);

        this.mockMvc.perform(post("/educations/addNewEducation/" + DEFAULT_S_PROFILE_ID)
                .param("profileId", DEFAULT_S_PROFILE_ID)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profiles/get/" + DEFAULT_S_PROFILE_ID));

        verify(educationService).addOrUpdateEducation(education);
    }

    @Test
    @DisplayName("If education exists then status ok and model has attr accomplishmentDto")
    void getEducation() throws Exception {
        when(educationService.getEducationById(education.getId())).thenReturn(Optional.of(education));
        when(mapperUtil.mapFromEducationToDto(any())).thenReturn(educationDto);

        this.mockMvc.perform(get("/educations/update/" + education.getId())
        )
                .andExpect(model().attributeExists("educationDto"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("After education update will redirect to all educations by profile id and verify that service worked")
    void updateEducation() throws Exception {
        when(mapperUtil.mapFromDtoToEducation(any())).thenReturn(education);

        this.mockMvc.perform(post("/educations/update/" + education.getId())
                .param("profileId", DEFAULT_S_PROFILE_ID)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profiles/get/" + DEFAULT_S_PROFILE_ID));

        verify(educationService).addOrUpdateEducation(education);
    }

    @Test
    @DisplayName("After education delete will redirect to all educations by profile id and verify that service worked")
    void deleteEducation() throws Exception {
        when(educationService.getEducationById(education.getId()))
                .thenReturn(Optional.of(education));
        this.mockMvc.perform(get("/educations/delete/" + education.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profiles/get/" + DEFAULT_S_PROFILE_ID));
        verify(educationService).deleteEducation(education);
    }

    @Test
    @DisplayName("Will return list of educations by profile id and Member name")
    void getEducationsByProfileId() throws Exception {
        when(educationService.getEducationsByProfileID(DEFAULT_L_PROFILE_ID)).thenReturn(List.of(education));
        when(mapperUtil.mapFromEducationToDto(education)).thenReturn(educationDto);
        when(profileService.getProfileById(DEFAULT_L_PROFILE_ID)).thenReturn(Optional.ofNullable(getProfileWithMember()));

        this.mockMvc.perform(get("/educations/getEducationsByProfileId/" + DEFAULT_S_PROFILE_ID)
        )
                .andExpect(model().attributeExists("educationDtoList"))
                .andExpect(model().attributeExists("profileId"))
                .andExpect(model().attributeExists("memberName"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Will return list of educations by profile id and NonexistentMember")
    void getEducationsByProfileIdWhenMemberNotExists() throws Exception {
        when(educationService.getEducationsByProfileID(DEFAULT_L_PROFILE_ID)).thenReturn(List.of(education));
        when(mapperUtil.mapFromEducationToDto(education)).thenReturn(educationDto);
        when(profileService.getProfileById(DEFAULT_L_PROFILE_ID)).thenReturn(getEmptyProfile());

        this.mockMvc.perform(get("/educations/getEducationsByProfileId/" + DEFAULT_S_PROFILE_ID)
        )
                .andExpect(model().attributeExists("educationDtoList"))
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

    private EducationDto getEducationDTO() {
        EducationDto dto = new EducationDto();
        dto.setProfileId(DEFAULT_L_PROFILE_ID);
        dto.setDescription(EDUCATION_DESCRIPTION);
        dto.setMemberName(MEMBER_NAME);
        return dto;
    }

    private Education getReadyEducation() {
        return Education.builder()
                .id(DEFAULT_L_PROFILE_ID)
                .description(EDUCATION_DESCRIPTION)
                .profile(Profile.builder().id(DEFAULT_L_PROFILE_ID).build())
                .build();
    }
}