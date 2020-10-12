package com.ra.course.linkedin.web.mapper.accomplishment;

import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.model.entity.profile.Accomplishment;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.web.dto.accomplishment.AccomplishmentDto;
import com.ra.course.linkedin.web.mapper.AccomplishmentMapperUtil;
import org.junit.jupiter.api.DisplayName;
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
class AccomplishmentMapperUtilTest {

    public static final Long DEFAULT_L_PROFILE_ID = 1L;
    public static final String PROFILE_DATE_STRING = "23-12-2017";
    public static final String MEMBER_NAME = "Vasya";
    public static final String ACCOMPLISHMENT_TITLE = "Accomplishment title";
    public static final String ACCOMPLISHMENT_DESCRIPTION = "Accomplishment description";
    public static final LocalDate LOCAL_DATE = LocalDate.parse(PROFILE_DATE_STRING, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    public static final long UNSIGNED_ID = 0;
    @MockBean
    private ProfileService profileService;
    @Autowired
    AccomplishmentMapperUtil mapper;

    @Test
    @DisplayName("Test AccomplishmentMapperUtil from accomplishment to dto direction")
    void mapFromAccomplishmentToDto() {
        //when
        AccomplishmentDto dto = mapper.mapFromAccomplishmentToDto(getReadyAccomplishment());
        //then
        assertEquals(dto, getAccomplishmentDTO());
    }

    @Test
    @DisplayName("Test AccomplishmentMapperUtil from dto to accomplishment direction")
    void mapFromDtoToAccomplishment() {
        //given
        Accomplishment expectedAccomplishment = getReadyAccomplishment();
        //when
        when(profileService.getProfileById(anyLong())).thenReturn(Optional.of(getProfile()));
        Accomplishment accomplishment = mapper.mapFromDtoToAccomplishment(getAccomplishmentDTO());
        //then
        assertEquals(expectedAccomplishment.getTitle(), accomplishment.getTitle());
        assertEquals(expectedAccomplishment.getProfile().getMember().getName(), accomplishment.getProfile().getMember().getName());
    }

    private Profile getProfile(){
        return Profile.builder()
                .id(DEFAULT_L_PROFILE_ID)
                .member(Member.builder().name(MEMBER_NAME).build())
                .build();
    }

    private AccomplishmentDto getAccomplishmentDTO(){
        AccomplishmentDto dto = new AccomplishmentDto();
        dto.setProfileId(DEFAULT_L_PROFILE_ID);
        dto.setDate(PROFILE_DATE_STRING);
        dto.setDescription(ACCOMPLISHMENT_DESCRIPTION);
        dto.setTitle(ACCOMPLISHMENT_TITLE);
        dto.setMemberName(MEMBER_NAME);
        return dto;
    }
    private Accomplishment getReadyAccomplishment(){
        return Accomplishment.builder()
                .id(UNSIGNED_ID)
                .date(LOCAL_DATE)
                .title(ACCOMPLISHMENT_TITLE)
                .description(ACCOMPLISHMENT_DESCRIPTION)
                .profile(Profile.builder().id(DEFAULT_L_PROFILE_ID).member(Member.builder().name("Vasya").build()).build())
                .build();
    }
}