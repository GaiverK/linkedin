package com.ra.course.linkedin.service.skill;

import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.model.entity.profile.Skill;
import com.ra.course.linkedin.persistence.skill.SkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SkillServiceImplMockTest {

    private static final long EXISTED_ID = 1L;
    private static final long NON_EXISTENT_ID = -1L;

    private final SkillRepository mockSkillRepository = mock(SkillRepository.class);
    private final SkillService skillService =
            new SkillServiceImpl(mockSkillRepository);

    private Skill skill;

    @BeforeEach
    public void setUp() {
        skill = createSkill();
    }

    @Test
    @DisplayName("When the skill is added or updated, " +
            "then should be called SkillRepository's save method")
    public void testAddOrUpdateSkill() {
        //when
        skillService.addOrUpdateSkill(skill);
        //then
        verify(mockSkillRepository).save(skill);
    }

    @Test
    @DisplayName("When the skill is deleted, then should be called " +
            "SkillRepository's delete method.")
    public void testDeleteSkill() {
        //when
        skillService.deleteSkill(skill);
        //then
        verify(mockSkillRepository).delete(skill);
    }

    @Test
    @DisplayName("When get existed skill, it should be returned.")
    public void testGetExistedSkill() {
        //when
        when(mockSkillRepository.getById(EXISTED_ID))
                .thenReturn(Optional.of(skill));
        Optional<Skill> result = skillService.getSkillById(
                skill.getId());
        //then
        assertEquals(Optional.of(skill), result);
        verify(mockSkillRepository).getById(skill.getId());
    }

    @Test
    @DisplayName("When get not existed skill, " +
            "then must be returned empty optional.")
    public void testGetNotExistedSkill() {
        //when
        Optional<Skill> skillOptional =
                skillService.getSkillById(NON_EXISTENT_ID);
        //then
        assertEquals(Optional.empty(), skillOptional);
        verify(mockSkillRepository).getById(NON_EXISTENT_ID);
    }

    @Test
    @DisplayName("When get skills with specific profileId, " +
            "then return list of skills with this profileId")
    void testGetAllSkillsByProfileID() {
        //given
        Skill skillWithProfileWithExistingId =
                Skill.builder()
                        .profile(Profile.builder().id(EXISTED_ID).build())
                        .build();
        //when
        when(skillService.getSkillsByProfileID(EXISTED_ID))
                .thenReturn(List.of(skillWithProfileWithExistingId));
        List<Skill> skillList = skillService.getSkillsByProfileID(EXISTED_ID);
        //then
        assertEquals(List.of(skillWithProfileWithExistingId), skillList);
        verify(mockSkillRepository).getSkillsByProfileID(
                skillWithProfileWithExistingId.getProfile().getId());
    }
    private Skill createSkill() {
        return Skill.builder()
                .id(EXISTED_ID)
                .build();
    }
}