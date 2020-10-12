package com.ra.course.linkedin.web.controller.skill;

import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.model.entity.profile.Skill;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.service.skill.SkillService;
import com.ra.course.linkedin.web.dto.skill.SkillDto;
import com.ra.course.linkedin.web.mapper.SkillMapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static com.ra.course.linkedin.web.controller.profile.ProfileControllerMockMvcTest.SOME_ID_22;
import static com.ra.course.linkedin.web.controller.profile.ProfileControllerMockMvcTest.SOME_PROFILE_ID;
import static com.ra.course.linkedin.web.controller.recommendation.RecommendationControllerMockMvcTest.SOME_ID_44;
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
public class SkillControllerMockMvcTest {

    @MockBean
    private SkillService skillService;
    @MockBean
    private SkillMapperUtil skillMapper;
    @MockBean
    private ProfileService profileService;

    @Autowired
    MockMvc mockMvc;

    private Profile profile;
    private Skill skill;
    private SkillDto skillDto;

    @BeforeEach
    void setUp() {
        profile = createProfile();
        skill = createSkill();
        skillDto = createSkillDto();
    }

    @Test
    @DisplayName("Expect status is Ok and model has attribute profileId")
    public void testGetAddSkill() throws Exception {
        //then
        mockMvc.perform(get("/skills/addNewSkill/" + profile.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("profileId"));
    }

    @Test
    @DisplayName("Expect status is Redirection, redirected url is" +
            "/profiles/get/ + profile.getId() " +
            "and skillService used method addOrUpdateSkill " +
            "with parameter skill")
    public void testPostAddSkill() throws Exception {
        //when
        when(skillMapper.mapFromDtoToSkill(any())).thenReturn(skill);
        //then
        mockMvc.perform(post("/skills/addNewSkill/" + profile.getId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(
                        "/profiles/get/"
                                + profile.getId()));

        verify(skillService).addOrUpdateSkill(skill);
    }

    @Test
    @DisplayName("Expect status is Ok and model has attribute skillDto")
    public void testGetSkill() throws Exception {
        //when
        when(skillService.getSkillById(skill.getId()))
                .thenReturn(ofNullable(skill));
        when(skillMapper.mapFromSkillToDto(skill)).thenReturn(skillDto);
        //then
        mockMvc.perform(
                get("/skills/update/" + skill.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("skillDto"));
    }

    @Test
    @DisplayName("Expect status is Redirection, redirected url is" +
            "/profiles/get/ + profile.getId()" +
            "and skillService used method addOrUpdateSkill " +
            "with parameter skill")
    public void testUpdateSkill() throws Exception {
        //when
        when(skillMapper.mapFromDtoToSkill(any())).thenReturn(skill);
        //then
        mockMvc.perform(post("/skills/update/" + skill.getId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(
                        redirectedUrl("/profiles/get/" +
                                skill.getProfile().getId()));

        verify(skillService).addOrUpdateSkill(skill);
    }

    @Test
    @DisplayName("Expect status is Redirection, redirected url is" +
            "/profiles/get/ + " +
            "skill.getProfile().getId() " +
            "and skillService used method deleteSkill " +
            "with parameter skill")
    public void testDeleteSkill() throws Exception {
        //when
        when(skillService.getSkillById(skill.getId()))
                .thenReturn(ofNullable(skill));
        //then
        mockMvc.perform(get("/skills/delete/" + skill.getId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(
                        redirectedUrl("/profiles/get/" +
                                skill.getProfile().getId()));

        verify(skillService).deleteSkill(skill);
    }

    @Test
    @DisplayName("Expect status is Ok and model has attributes: " +
            "skillDtos, profileId, memberName")
    public void testGetSkillsByProfileIdWhenMemberExists()
            throws Exception {
        //when
        when(skillService.getSkillsByProfileID(profile.getId()))
                .thenReturn(List.of(skill));
        when(skillMapper.mapFromSkillToDto(skill)).thenReturn(skillDto);
        when(profileService.getProfileById(profile.getId()))
                .thenReturn(ofNullable(profile));
        //then
        mockMvc.perform(get("/skills/getSkillsByProfileId/"
                + profile.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("skillDtos"))
                .andExpect(model().attributeExists("profileId"))
                .andExpect(model().attributeExists("memberName"));
    }

    @Test
    @DisplayName("Expect status is Ok and model has attributes: " +
            "skillDtos, profileId " +
            "and memberName is NonexistentMember")
    public void testGetSkillsByProfileIdWhenMemberDoesNotExist()
            throws Exception {
        //when
        when(skillService.getSkillsByProfileID(profile.getId()))
                .thenReturn(List.of(skill));
        when(skillMapper.mapFromSkillToDto(skill)).thenReturn(skillDto);
        when(profileService.getProfileById(profile.getId()))
                .thenReturn(Optional.empty());
        //then
        mockMvc.perform(get("/skills/getSkillsByProfileId/"
                + profile.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("skillDtos"))
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
                .build();
    }

    private Skill createSkill() {
        return Skill.builder()
                .profile(profile)
                .id(SOME_ID_22)
                .build();
    }

    private SkillDto createSkillDto() {
        skillDto = new SkillDto();
        skillDto.setProfileId(profile.getId());
        skillDto.setId(SOME_ID_22);

        return skillDto;
    }
}
