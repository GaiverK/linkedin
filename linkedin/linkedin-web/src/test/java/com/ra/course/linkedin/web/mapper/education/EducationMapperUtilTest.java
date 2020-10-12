package com.ra.course.linkedin.web.mapper.education;

import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.model.entity.profile.Education;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.web.dto.education.EducationDto;
import com.ra.course.linkedin.web.mapper.EducationMapperUtil;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class EducationMapperUtilTest {

    public static final Long DEFAULT_L_PROFILE_ID = 1L;
    public static final String MEMBER_NAME = "Vasya";
    public static final long UNSIGNED_ID = 0;
    public static final String EDUCATION_DESCRIPTION = "Education description";

    @MockBean
    private ProfileService profileService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    EducationMapperUtil mapper;

    @Test
    void mapFromEducationToDto() {
        //when
        EducationDto dto = mapper.mapFromEducationToDto(getReadyEducation());
        //then
        assertEquals(dto, getEducationDTO());
    }

    @Test
    void mapFromDtoToEducation() {
        //given
        Education expectedEducation = getReadyEducation();
        //when
        when(profileService.getProfileById(anyLong())).thenReturn(Optional.of(getProfile()));
        Education education = mapper.mapFromDtoToEducation(getEducationDTO());
        //then
        assertEquals(expectedEducation.getDescription(), education.getDescription());
        assertEquals(expectedEducation.getProfile().getMember().getName(), education.getProfile().getMember().getName());
    }

    private Profile getProfile() {
        return Profile.builder()
                .id(DEFAULT_L_PROFILE_ID)
                .member(Member.builder().name(MEMBER_NAME).build())
                .build();
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
                .id(UNSIGNED_ID)
                .description(EDUCATION_DESCRIPTION)
                .profile(Profile.builder().id(DEFAULT_L_PROFILE_ID)
                        .member(Member.builder().name("Vasya").build()).build())
                .build();
    }
}