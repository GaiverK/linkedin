package com.ra.course.linkedin.web.controller.accomplishment;

import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.model.entity.profile.Accomplishment;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.service.accomplishment.AccomplishmentService;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.web.dto.accomplishment.AccomplishmentDto;
import com.ra.course.linkedin.web.mapper.AccomplishmentMapperUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
class AccomplishmentControllerMockMvcTest {

    public static final String DEFAULT_S_PROFILE_ID = "1";
    public static final Long DEFAULT_L_PROFILE_ID = 1L;
    public static final String PROFILE_DATE_STRING = "23-12-2017";
    public static final String MEMBER_NAME = "Vasya";
    public static final String ACCOMPLISHMENT_TITLE = "Accomplishment title";
    public static final String ACCOMPLISHMENT_DESCRIPTION = "Accomplishment description";
    public static final LocalDate LOCAL_DATE = LocalDate.parse(PROFILE_DATE_STRING, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

    @MockBean
    private AccomplishmentService accomplishService;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private AccomplishmentMapperUtil mapperUtil;

    @Autowired
    private MockMvc mockMvc;

    private final Accomplishment accomplishment = getReadyAccomplishment();
    private final AccomplishmentDto accomplishmentDto = getAccomplishmentDTO();

    @Test
    @DisplayName("If add accomplishment get request, then status 200 and attr profileId exists")
    void addAccomplishmentGetTest() throws Exception {
        this.mockMvc.perform(get("/accomplishments/addNewAccomplishment/" + DEFAULT_S_PROFILE_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("profileId"));
    }

    @Test
    @DisplayName("If add accomplishment Post request, then check status 200 and redirected url")
    void addAccomplishmentPostTest() throws Exception {
        when(mapperUtil.mapFromDtoToAccomplishment(any())).thenReturn(accomplishment);

        this.mockMvc.perform(post("/accomplishments/addNewAccomplishment/" + DEFAULT_S_PROFILE_ID)
                .param("profileId", DEFAULT_S_PROFILE_ID)
                .param("date", PROFILE_DATE_STRING)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profiles/get/" + DEFAULT_S_PROFILE_ID));

        verify(accomplishService).addOrUpdateAccomplishment(accomplishment);
    }

    @Test
    @DisplayName("If accomplishment exists then status ok and model has attr accomplishmentDto")
    void getAccomplishmentWhenExists() throws Exception {
        when(accomplishService.getAccomplishmentById(accomplishment.getId())).thenReturn(Optional.of(accomplishment));
        when(mapperUtil.mapFromAccomplishmentToDto(any())).thenReturn(accomplishmentDto);

        this.mockMvc.perform(get("/accomplishments/update/" + accomplishment.getId())
        )
                .andExpect(model().attributeExists("accomplishmentDto"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("After accomplishment update will redirect to all accomplishments by profile id and verify that service worked")
    void updateAccomplishment() throws Exception {
        when(mapperUtil.mapFromDtoToAccomplishment(any())).thenReturn(accomplishment);

        this.mockMvc.perform(post("/accomplishments/update/" + accomplishment.getId())
                .param("profileId", DEFAULT_S_PROFILE_ID)
                .param("date", PROFILE_DATE_STRING)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profiles/get/" + DEFAULT_S_PROFILE_ID));

        verify(accomplishService).addOrUpdateAccomplishment(accomplishment);
    }

    @Test
    @DisplayName("After accomplishment delete will redirect to all accomplishments by profile id and verify that service worked")
    void deleteAccomplishment() throws Exception {
        when(accomplishService.getAccomplishmentById(accomplishment.getId())).thenReturn(Optional.of(accomplishment));
        this.mockMvc.perform(get("/accomplishments/delete/" + accomplishment.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profiles/get/" + DEFAULT_S_PROFILE_ID));
        verify(accomplishService).deleteAccomplishment(accomplishment);
    }

    @Test
    @DisplayName("Will return list of accomplishments by profile id and Member name")
    void getAccomplishmentsByProfileIdWhenMemberNameExists() throws Exception {
        when(accomplishService.getAccomplishmentsByProfileID(DEFAULT_L_PROFILE_ID)).thenReturn(List.of(accomplishment));
        when(mapperUtil.mapFromAccomplishmentToDto(accomplishment)).thenReturn(accomplishmentDto);
        when(profileService.getProfileById(DEFAULT_L_PROFILE_ID)).thenReturn(Optional.ofNullable(getProfileWithMember()));

        this.mockMvc.perform(get("/accomplishments/getAccomplishmentsByProfileId/" + DEFAULT_S_PROFILE_ID)
        )
                .andExpect(model().attributeExists("accomplishmentDtos"))
                .andExpect(model().attributeExists("profileId"))
                .andExpect(model().attributeExists("memberName"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Will return list of accomplishments by profile id and NonexistentMember")
    void getAccomplishmentsByProfileIdWhenMemberNameNotExists() throws Exception {
        when(accomplishService.getAccomplishmentsByProfileID(DEFAULT_L_PROFILE_ID)).thenReturn(List.of(accomplishment));
        when(mapperUtil.mapFromAccomplishmentToDto(accomplishment)).thenReturn(accomplishmentDto);
        when(profileService.getProfileById(DEFAULT_L_PROFILE_ID)).thenReturn(getEmptyProfile());

        this.mockMvc.perform(get("/accomplishments/getAccomplishmentsByProfileId/" + DEFAULT_S_PROFILE_ID)
        )
                .andExpect(model().attributeExists("accomplishmentDtos"))
                .andExpect(model().attributeExists("profileId"))
                .andExpect(model().attribute("memberName", "NonexistentMember"))
                .andExpect(status().isOk());
    }

    private AccomplishmentDto getAccomplishmentDTO() {
        AccomplishmentDto dto = new AccomplishmentDto();
        dto.setProfileId(DEFAULT_L_PROFILE_ID);
        dto.setDate(PROFILE_DATE_STRING);
        dto.setDescription(ACCOMPLISHMENT_DESCRIPTION);
        dto.setTitle(ACCOMPLISHMENT_TITLE);
        dto.setMemberName(MEMBER_NAME);
        return dto;
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

    private Accomplishment getReadyAccomplishment() {
        return Accomplishment.builder()
                .id(DEFAULT_L_PROFILE_ID)
                .date(LOCAL_DATE)
                .title(ACCOMPLISHMENT_TITLE)
                .description(ACCOMPLISHMENT_DESCRIPTION)
                .profile(Profile.builder().id(DEFAULT_L_PROFILE_ID).build())
                .build();
    }
}