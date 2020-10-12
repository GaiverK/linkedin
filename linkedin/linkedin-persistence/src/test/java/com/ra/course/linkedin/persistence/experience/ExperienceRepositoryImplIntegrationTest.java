package com.ra.course.linkedin.persistence.experience;

import com.ra.course.linkedin.model.entity.profile.Experience;
import com.ra.course.linkedin.model.entity.profile.Profile;
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
public class ExperienceRepositoryImplIntegrationTest {

    private static final long NON_EXISTENT_ID = -1L;

    @Autowired
    ExperienceRepository experienceRepository;
    @Autowired
    ProfileRepository profileRepository;

    private Profile profile;

    private List<Experience> searchedExperiences;

    @BeforeEach
    public void setUp() {
        profile = new Profile();
        profileRepository.save(profile);
        searchedExperiences = createSearchedExperiences();
        searchedExperiences.forEach(experienceRepository::save);
    }

    @AfterEach
    public void tearDown() {
        profileRepository.delete(profile);
        searchedExperiences.forEach(experienceRepository::delete);
    }

    @Test
    @DisplayName("When searched experiences exist, then must be returned their list")
    void testWhenSearchedExperiencesExist() {
        //when
        List<Experience> experiencesByProfileId =
                experienceRepository.getExperiencesByProfileID(profile.getId());
        //then
        assertAll(
                () -> assertEquals(experiencesByProfileId.size(), 3),
                () -> assertEquals(experiencesByProfileId.get(0).getDescription(),
                        createSearchedExperiences().get(0).getDescription()),
                () -> assertEquals(experiencesByProfileId.get(1).getDescription(),
                        createSearchedExperiences().get(1).getDescription()),
                () -> assertEquals(experiencesByProfileId.get(2).getDescription(),
                        createSearchedExperiences().get(2).getDescription())
        );
    }

    @Test
    @DisplayName("When searched experiences do not exist, " +
            "then must be returned empty list")
    void testWhenSearchedExperiencesDoNotExist() {
        //when
        List<Experience> experiencesByProfileId =
                experienceRepository.getExperiencesByProfileID(NON_EXISTENT_ID);
        //then
        assertEquals(experiencesByProfileId.size(), 0);
    }

    private List<Experience> createSearchedExperiences() {
        final List<Experience> searchedExperiences = new ArrayList<>();
        Experience experience = Experience.builder()
                .profile(profile)
                .description("experience")
                .build();
        Experience experience1 = Experience.builder()
                .profile(profile)
                .description("experience1")
                .build();
        Experience experience2 = Experience.builder()
                .profile(profile)
                .description("experience2")
                .build();
        searchedExperiences.add(experience);
        searchedExperiences.add(experience1);
        searchedExperiences.add(experience2);

        return searchedExperiences;
    }

}
