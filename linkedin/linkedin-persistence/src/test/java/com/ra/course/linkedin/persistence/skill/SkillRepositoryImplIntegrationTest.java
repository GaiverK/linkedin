package com.ra.course.linkedin.persistence.skill;

import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.model.entity.profile.Skill;
import com.ra.course.linkedin.persistence.configuration.RepositoryConfiguration;
import com.ra.course.linkedin.persistence.profile.ProfileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = RepositoryConfiguration.class)
public class SkillRepositoryImplIntegrationTest {

    private static final long NON_EXISTENT_ID = -1L;

    @Autowired
    SkillRepository skillRepository;
    @Autowired
    ProfileRepository profileRepository;

    private Profile profile;

    private List<Skill> searchedSkills;

    @BeforeEach
    public void setUp() {
        profile = new Profile();
        profileRepository.save(profile);
        searchedSkills = createSearchedSkills();
        searchedSkills.forEach(skillRepository::save);
    }

    @AfterEach
    public void tearDown() {
        profileRepository.delete(profile);
        searchedSkills.forEach(skillRepository::delete);
    }

    @Test
    @DisplayName("When searched skills exist, then must be returned their list")
    void testWhenSearchedSkillsExist() {
        //when
        List<Skill> skillsByProfileId =
                skillRepository.getSkillsByProfileID(profile.getId());
        //then
        assertAll(
                () -> assertEquals(skillsByProfileId.size(), 3),
                () -> assertEquals(skillsByProfileId.get(0).getName(),
                        createSearchedSkills().get(0).getName()),
                () -> assertEquals(skillsByProfileId.get(1).getName(),
                        createSearchedSkills().get(1).getName()),
                () -> assertEquals(skillsByProfileId.get(2).getName(),
                        createSearchedSkills().get(2).getName())
        );
    }

    @Test
    @DisplayName("When searched skills do not exist, " +
            "then must be returned empty list")
    void testWhenSearchedSkillsDoNotExist() {
        //when
        List<Skill> skillsByProfileId =
                skillRepository.getSkillsByProfileID(NON_EXISTENT_ID);
        //then
        assertEquals(skillsByProfileId.size(), 0);
    }

    private List<Skill> createSearchedSkills() {
        List<Skill> searchedSkills = new ArrayList<>();
        Skill skill = Skill.builder()
                .profile(profile)
                .name("skill")
                .build();
        Skill skill1 = Skill.builder()
                .profile(profile)
                .name("skill1")
                .build();
        Skill skill2 = Skill.builder()
                .profile(profile)
                .name("skill2")
                .build();
        searchedSkills.add(skill);
        searchedSkills.add(skill1);
        searchedSkills.add(skill2);

        return searchedSkills;
    }
}
