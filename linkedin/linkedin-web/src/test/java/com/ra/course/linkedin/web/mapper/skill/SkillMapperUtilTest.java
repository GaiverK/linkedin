package com.ra.course.linkedin.web.mapper.skill;

import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.model.entity.profile.Skill;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.web.dto.skill.SkillDto;
import com.ra.course.linkedin.web.mapper.SkillMapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.ra.course.linkedin.web.controller.profile.ProfileControllerMockMvcTest.SOME_ID_22;
import static com.ra.course.linkedin.web.controller.profile.ProfileControllerMockMvcTest.SOME_PROFILE_ID;
import static com.ra.course.linkedin.web.controller.recommendation.RecommendationControllerMockMvcTest.SOME_ID_44;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SkillMapperUtilTest {

    @MockBean
    private ProfileService profileService;
    @Autowired
    private SkillMapperUtil skillMapper;

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
    @DisplayName("Expect skillDto is mapped properly")
    public void testMapFromSkillToDto() {
        //when
        SkillDto result = skillMapper.mapFromSkillToDto(skill);
        //then
        assertEquals(skillDto, result);
    }

    @Test
    @DisplayName("Expect skill has correct parameters ")
    public void testMapFromDtoToSkill() {
        //when
        when(profileService.getProfileById(skillDto.getProfileId()))
                .thenReturn(ofNullable(profile));
        Skill result = skillMapper.mapFromDtoToSkill(skillDto);
        //then
        assertAll(
                () -> assertEquals(skill.getProfile(), result.getProfile()),
                () -> assertEquals(skill.getId(), result.getId()),
                () -> assertEquals(skill.getName(), result.getName()));
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
                .name("name")
                .build();
    }

    private SkillDto createSkillDto() {
        skillDto = new SkillDto();
        skillDto.setProfileId(profile.getId());
        skillDto.setId(SOME_ID_22);
        skillDto.setName("name");
        skillDto.setMemberName("Pushkin");

        return skillDto;
    }
}
