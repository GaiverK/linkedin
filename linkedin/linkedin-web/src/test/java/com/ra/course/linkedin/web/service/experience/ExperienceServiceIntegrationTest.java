package com.ra.course.linkedin.web.service.experience;

import com.ra.course.linkedin.model.entity.profile.Experience;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.persistence.experience.ExperienceRepository;
import com.ra.course.linkedin.persistence.profile.ProfileRepository;
import com.ra.course.linkedin.service.experience.ExperienceService;
import com.ra.course.linkedin.service.profile.ProfileService;
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
public class ExperienceServiceIntegrationTest {

    private static final long NON_EXISTENT_ID = -1L;
    public static final String NEW_DESCRIPTION = "New description";

    @Autowired
    private ExperienceService experienceService;
    @Autowired
    private ExperienceRepository experienceRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileRepository profileRepository;

    private Experience experience;
    private Profile profile;

    @BeforeEach
    void setUp() {
        profile = new Profile();
        profileService.saveProfile(profile);
        experience = createExperience();
        experienceService.addOrUpdateExperience(experience);
    }
    @AfterEach
    void tearDown() {
        tryToDelete(experienceRepository, experience);
        tryToDelete(profileRepository, profile);
    }

    @Test
    @DisplayName("When the experience is added or updated, " +
            "then it should be saved in repository")
    void testAddOrUpdateExperience() {
        //then
        final Experience experienceFromRepo =
                experienceService.getExperienceById(experience.getId()).get();
        assertEquals(experienceFromRepo, experience);
        //when
        experience.setDescription(NEW_DESCRIPTION);
        experienceService.addOrUpdateExperience(experience);
        //then
        assertEquals(experience.getDescription(), NEW_DESCRIPTION);
    }

    @Test
    @DisplayName("When experience was deleted, " +
            "then this experience should be absent in repository")
    public void testDeleteExperience() {
        assertTrue(getAllExperiencesFromRepository().contains(experience));
        //when
        experienceService.deleteExperience(experience);
        //then
        assertFalse(getAllExperiencesFromRepository().contains(experience));
    }

    @Test
    @DisplayName("When get existed experience, it should be returned.")
    public void testGetExistedExperience() {
        //when
        Experience experienceFromRepo =
                experienceService.getExperienceById(experience.getId()).get();
        //then
        assertEquals(experience, experienceFromRepo);
    }

    @Test
    @DisplayName("When get not existed experience, " +
            "then must be returned empty optional.")
    public void testGetNotExistedExperience() {
        //when
        Optional<Experience> experienceOptional =
                experienceService.getExperienceById(NON_EXISTENT_ID);
        //then
        assertTrue(experienceOptional.isEmpty());
    }

    @Test
    @DisplayName("When get experiences with specific profileId, " +
            "then return list of experiences with this profileId")
    void testGetAllExperiencesByProfileID() {
        //when
        List<Experience> experienceList =
                experienceService.getExperiencesByProfileID(profile.getId());
        //then
        assertEquals(List.of(experience), experienceList);
    }

    private Experience createExperience() {
        return Experience.builder()
                .profile(profile)
                .build();
    }

    private List<Experience> getAllExperiencesFromRepository() {
        return new CopyOnWriteArrayList<>((CopyOnWriteArrayList<Experience>)
                experienceRepository.getAll());
    }
}
