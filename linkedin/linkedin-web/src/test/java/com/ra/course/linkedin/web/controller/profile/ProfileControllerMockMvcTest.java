package com.ra.course.linkedin.web.controller.profile;

import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.web.dto.member.MemberPageDto;
import com.ra.course.linkedin.web.dto.profile.ProfileDto;
import com.ra.course.linkedin.web.mapper.ProfileMapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

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
public class ProfileControllerMockMvcTest {

    public static final String OLD_SUMMARY = "OLD_SUMMARY";
    public static final long SOME_PROFILE_ID = 11L;
    public static final long SOME_ID_22 = 22L;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private ProfileMapperUtil profileMapperUtil;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private Profile profile;
    private ProfileDto profileDto;

    @BeforeEach
    void setUp() {
        profile = createProfile();
        profileDto = createProfileDto();
    }

    @Test
    @DisplayName("Expect status is Ok and model has attribute profile")
    public void testGetProfile() throws Exception {
        //when
        when(profileService.getProfileById(profile.getId()))
                .thenReturn(ofNullable(profile));
        when(profileMapperUtil.mapFromProfileToDto(profile))
                .thenReturn(profileDto);
        //then
        mockMvc.perform(get("/profiles/get/" + profile.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("profile"));
    }

    @Test
    @DisplayName("Expect status is Redirection, redirected url is" +
            "/profiles/get/{profile.getId()} " +
            "and profileService used method saveProfile " +
            "with parameter profile")
    public void testUpdateProfileSummary() throws Exception {
        //when
        when(profileMapperUtil.mapFromDtoToProfile(any()))
                .thenReturn(profile);
        //then
        mockMvc.perform(post("/profiles/get/" + profile.getId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(
                        "/profiles/get/" + profile.getId()));

        verify(profileService).saveProfile(profile);
    }

    private Profile createProfile() {
        Member pushkin = Member.builder()
                .name("Pushkin")
                .id(SOME_ID_22)
                .build();
        pushkin.setIncomingConnectionInvitations(new ArrayList<>());

        return Profile.builder()
                .summary(OLD_SUMMARY)
                .id(SOME_PROFILE_ID)
                .member(pushkin)
                .build();
    }

    private ProfileDto createProfileDto() {
        profileDto = new ProfileDto();
        profileDto.setSummary(profile.getSummary());
        profileDto.setMemberId(profile.getMember().getId());
        profileDto.setMemberName(profile.getMember().getName());
        profileDto.setMemberAppliedJobPosting(new ArrayList<>());
        profileDto.setMemberSavedJobs(new ArrayList<>());
        profileDto.setMemberFollowers(new ArrayList<>());
        profileDto.setMemberFollowingMembers(new ArrayList<>());
        profileDto.setMember(mapper.map(profile.getMember(), MemberPageDto.class));
        profileDto.setId(profile.getId());
        return profileDto;
    }

}
