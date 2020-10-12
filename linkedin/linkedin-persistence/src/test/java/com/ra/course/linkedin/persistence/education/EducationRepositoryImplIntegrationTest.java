package com.ra.course.linkedin.persistence.education;

import com.ra.course.linkedin.model.entity.profile.Education;
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
public class EducationRepositoryImplIntegrationTest {

    private static final long NON_EXISTENT_ID = -1L;

    @Autowired
    EducationRepository educationRepository;
    @Autowired
    ProfileRepository profileRepository;

    private Profile profile;

    private List<Education> searchedEducations;

    @BeforeEach
    public void setUp() {
        profile = new Profile();
        profileRepository.save(profile);
        searchedEducations = createSearchedEducations();
        searchedEducations.forEach(educationRepository::save);
    }

    @AfterEach
    public void tearDown() {
        profileRepository.delete(profile);
        searchedEducations.forEach(educationRepository::delete);
    }

    @Test
    @DisplayName("When searched educations exist, then must be returned their list")
    void testWhenSearchedEducationsExist() {
        //when
        List<Education> educationsByProfileId =
                educationRepository.getEducationsByProfileID(
                        profile.getId());
        //then
        assertAll(
                () -> assertEquals(educationsByProfileId.size(), 3),
                () -> assertEquals(educationsByProfileId.get(0).getDescription(),
                        createSearchedEducations().get(0).getDescription()),
                () -> assertEquals(educationsByProfileId.get(1).getDescription(),
                        createSearchedEducations().get(1).getDescription()),
                () -> assertEquals(educationsByProfileId.get(2).getDescription(),
                        createSearchedEducations().get(2).getDescription())
        );
    }


    @Test
    @DisplayName("When searched educations do not exist, " +
            "then must be returned empty list")
    void testWhenSearchedEducationsDoNotExist() {
        //when
        List<Education> educationsByProfileId =
                educationRepository.getEducationsByProfileID(NON_EXISTENT_ID);
        //then
        assertEquals(educationsByProfileId.size(), 0);
    }


    private List<Education> createSearchedEducations() {
        List<Education> searchedEducations = new ArrayList<>();
        Education education = Education.builder()
                .profile(profile)
                .description("education")
                .build();
        Education education1 = Education.builder()
                .profile(profile)
                .description("education1")
                .build();
        Education education2 = Education.builder()
                .profile(profile)
                .description("education2")
                .build();
        searchedEducations.add(education);
        searchedEducations.add(education1);
        searchedEducations.add(education2);

        return searchedEducations;
    }
}
