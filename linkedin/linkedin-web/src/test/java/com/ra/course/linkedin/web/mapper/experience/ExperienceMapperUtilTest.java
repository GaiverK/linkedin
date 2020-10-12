package com.ra.course.linkedin.web.mapper.experience;

import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.model.entity.profile.Experience;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.web.dto.experience.ExperienceDto;
import com.ra.course.linkedin.web.mapper.ExperienceMapperUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class ExperienceMapperUtilTest {

    public static final Long DEFAULT_L_PROFILE_ID = 1L;
    public static final String EXPERIENCE_DATE_FROM = "23-12-2017";
    public static final String EXPERIENCE_DATE_TO = "23-12-2019";
    public static final String MEMBER_NAME = "Vasya";
    public static final String ACCOMPLISHMENT_TITLE = "Accomplishment title";
    public static final String ACCOMPLISHMENT_DESCRIPTION = "Accomplishment description";
    public static final LocalDate LOCAL_DATE_FROM = LocalDate.parse(EXPERIENCE_DATE_FROM, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    public static final LocalDate LOCAL_DATE_TO = LocalDate.parse(EXPERIENCE_DATE_TO, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    public static final long UNSIGNED_ID = 0;
    @MockBean
    private ProfileService profileService;
    @Autowired
    ExperienceMapperUtil mapper;

    @Test
    void mapFromExperienceToDto() {
        //when
        ExperienceDto dto = mapper.mapFromExperienceToDto(getReadyExperience());
        //then
        assertEquals(dto, getExperienceDTO());
    }

    @Test
    void mapFromDtoToExperience() {
        //given
        Experience expectedExperience = getReadyExperience();
        //when
        when(profileService.getProfileById(anyLong())).thenReturn(Optional.of(getProfile()));
        Experience experience = mapper.mapFromDtoToExperience(getExperienceDTO());
        //then
        assertEquals(expectedExperience.getDescription(), experience.getDescription());
        assertEquals(expectedExperience.getProfile().getMember().getName(), experience.getProfile().getMember().getName());
    }

    private Profile getProfile() {
        return Profile.builder()
                .id(DEFAULT_L_PROFILE_ID)
                .member(Member.builder().name(MEMBER_NAME).build())
                .build();
    }

    private ExperienceDto getExperienceDTO() {
        ExperienceDto dto = new ExperienceDto();
        dto.setProfileId(DEFAULT_L_PROFILE_ID);
        dto.setDescription(ACCOMPLISHMENT_DESCRIPTION);
        dto.setTitle(ACCOMPLISHMENT_TITLE);
        dto.setFrom(EXPERIENCE_DATE_FROM);
        dto.setTo(EXPERIENCE_DATE_TO);
        dto.setMemberName(MEMBER_NAME);
        return dto;
    }

    private Experience getReadyExperience() {
        return Experience.builder()
                .id(UNSIGNED_ID)
                .title(ACCOMPLISHMENT_TITLE)
                .from(LOCAL_DATE_FROM)
                .to(LOCAL_DATE_TO)
                .description(ACCOMPLISHMENT_DESCRIPTION)
                .profile(Profile.builder().id(DEFAULT_L_PROFILE_ID).member(Member.builder().name("Vasya").build()).build())
                .build();
    }
}