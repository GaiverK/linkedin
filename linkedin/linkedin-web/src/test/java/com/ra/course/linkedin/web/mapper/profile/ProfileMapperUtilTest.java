package com.ra.course.linkedin.web.mapper.profile;

import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.service.member.MemberService;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.web.dto.member.MemberPageDto;
import com.ra.course.linkedin.web.dto.profile.ProfileDto;
import com.ra.course.linkedin.web.mapper.ProfileMapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;

import static com.ra.course.linkedin.web.controller.profile.ProfileControllerMockMvcTest.SOME_ID_22;
import static com.ra.course.linkedin.web.controller.profile.ProfileControllerMockMvcTest.SOME_PROFILE_ID;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProfileMapperUtilTest {

    @MockBean
    private ProfileService profileService;
    @MockBean
    private MemberService memberService;
    @Autowired
    private ProfileMapperUtil profileMapper;
    @Autowired
    private ModelMapper modelMapper;

    private Profile profile;
    private ProfileDto profileDto;

    @BeforeEach
    void setUp() {
        profile = createProfile();
        profileDto = createProfileDto();
    }

    @Test
    @DisplayName("Expect profileDto is mapped properly")
    public void testMapFromProfileToDto() {
        //when
        ProfileDto result = profileMapper.mapFromProfileToDto(profile);
        //then
        assertEquals(profileDto, result);
    }

    @Test
    @DisplayName("Expect profile has correct parameters ")
    public void testMapFromDtoToProfile() {
        //when
        when(profileService.getProfileById(profileDto.getId()))
                .thenReturn(ofNullable(profile));
        when(memberService.getMemberById(profileDto.getMember().getId()))
                .thenReturn(ofNullable(profile.getMember()));
        Profile result = profileMapper.mapFromDtoToProfile(profileDto);
        //then
        assertAll(
                () -> assertEquals(profile.getId(), result.getId()),
                () -> assertEquals(profile.getMember(), result.getMember()),
                () -> assertEquals(profile.getSummary(), result.getSummary()));
    }

    private Profile createProfile() {
        Member pushkin = Member.builder()
                .name("Pushkin")
                .id(SOME_ID_22)
                .build();
        pushkin.setIncomingConnectionInvitations(new ArrayList<>());

        Profile profile = Profile.builder()
                .id(SOME_PROFILE_ID)
                .member(pushkin)
                .summary("Summary")
                .build();

        pushkin.setProfile(profile);

        return profile;
    }

    private ProfileDto createProfileDto() {
        profileDto = new ProfileDto();
        profileDto.setId(profile.getId());
        profileDto.setMemberId(profile.getMember().getId());
        profileDto.setMemberName(profile.getMember().getName());
        profileDto.setSummary(profile.getSummary());
        profileDto.setMemberAppliedJobPosting(new ArrayList<>());
        profileDto.setMemberSavedJobs(new ArrayList<>());
        profileDto.setMemberFollowers(new ArrayList<>());
        profileDto.setMemberFollowingMembers(new ArrayList<>());
        profileDto.setMember(modelMapper.map(profile.getMember(), MemberPageDto.class));

        return profileDto;
    }
}
