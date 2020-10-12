package com.ra.course.linkedin.web.service.skill;

import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.model.entity.profile.Skill;
import com.ra.course.linkedin.persistence.profile.ProfileRepository;
import com.ra.course.linkedin.persistence.skill.SkillRepository;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.service.skill.SkillService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ra.course.linkedin.web.service.ServiceIntegrationTestUtils.tryToDelete;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SkillServiceImplIntegrationTest {

    private static final long NON_EXISTENT_ID = -1L;
    public static final String NEW_NAME = "New name";

    @Autowired
    private SkillService skillService;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileRepository profileRepository;

    private Skill skill;
    private Profile profile;

    @BeforeEach
    void setUp() {
        profile = new Profile();
        profileService.saveProfile(profile);
        skill = createSkill();
        skillService.addOrUpdateSkill(skill);
    }

    @AfterEach
    void tearDown() {
        tryToDelete(skillRepository, skill);
        tryToDelete(profileRepository, profile);
    }

    @Test
    @DisplayName("When the skill is added or updated, " +
            "then it should be saved in repository")
    void testAddOrUpdateSkill() {
        //then
        final Skill skillFromRepo =
                skillService.getSkillById(skill.getId()).get();
        assertEquals(skillFromRepo, skill);
        //when
        skill.setName(NEW_NAME);
        skillService.addOrUpdateSkill(skill);
        //then
        assertEquals(skill.getName(), NEW_NAME);
    }

    @Test
    @DisplayName("When skill was deleted, " +
            "then this skill should be absent in repository")
    public void testDeleteSkill() {
        assertTrue(getAllSkillsFromRepository().contains(skill));
        //when
        skillService.deleteSkill(skill);
        //then
        assertFalse(getAllSkillsFromRepository().contains(skill));
    }

    @Test
    @DisplayName("When get existed skill, it should be returned.")
    public void testGetExistedSkill() {
        //when
        Skill skillFromRepo =
                skillService.getSkillById(skill.getId()).get();
        //then
        assertEquals(skill, skillFromRepo);
    }

    @Test
    @DisplayName("When get not existed skill, " +
            "then must be returned empty optional.")
    public void testGetNotExistedSkill() {
        //when
        Optional<Skill> skillOptional =
                skillService.getSkillById(NON_EXISTENT_ID);
        //then
        assertTrue(skillOptional.isEmpty());
    }

    @Test
    @DisplayName("When get skills with specific profileId, " +
            "then return list of skills with this profileId")
    void testGetAllSkillsByProfileID() {
        //when
        List<Skill> skillList =
                skillService.getSkillsByProfileID(profile.getId());
        //then
        assertEquals(List.of(skill), skillList);
    }

    private Skill createSkill() {
        return Skill.builder()
                .profile(profile)
                .build();
    }

    private List<Skill> getAllSkillsFromRepository() {
        return new CopyOnWriteArrayList<>((CopyOnWriteArrayList<Skill>)
                skillRepository.getAll());
    }
}
