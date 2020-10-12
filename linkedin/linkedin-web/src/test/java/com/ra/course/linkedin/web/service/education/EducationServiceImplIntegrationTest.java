package com.ra.course.linkedin.web.service.education;

import com.ra.course.linkedin.model.entity.profile.Education;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.persistence.education.EducationRepository;
import com.ra.course.linkedin.persistence.profile.ProfileRepository;
import com.ra.course.linkedin.service.education.EducationService;
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
public class EducationServiceImplIntegrationTest {

    private static final long NON_EXISTENT_ID = -1L;
    public static final String NEW_DESCRIPTION = "New description";

    @Autowired
    private EducationService educationService;
    @Autowired
    private EducationRepository educationRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileRepository profileRepository;

    private Education education;
    private Profile profile;

    @BeforeEach
    void setUp() {
        profile = new Profile();
        profileService.saveProfile(profile);
        education = createEducation();
        educationService.addOrUpdateEducation(education);
    }

    @AfterEach
    void tearDown() {
        tryToDelete(educationRepository, education);
        tryToDelete(profileRepository, profile);
    }

    @Test
    @DisplayName("When the education is added or updated, " +
            "then it should be saved in repository")
    void testAddOrUpdateEducation() {
        //then
        final Education educationFromRepo =
                educationService.getEducationById(education.getId()).get();
        assertEquals(educationFromRepo, education);
        //when
        education.setDescription(NEW_DESCRIPTION);
        educationService.addOrUpdateEducation(education);
        //then
        assertEquals(education.getDescription(), NEW_DESCRIPTION);
    }

    @Test
    @DisplayName("When education was deleted, " +
            "then this education should be absent in repository")
    public void testDeleteEducation() {
        assertTrue(getAllEducationsFromRepository().contains(education));
        //when
        educationService.deleteEducation(education);
        //then
        assertFalse(getAllEducationsFromRepository().contains(education));
    }

    @Test
    @DisplayName("When get existed education, it should be returned.")
    public void testGetExistedEducation() {
        //when
        Education educationFromRepo =
                educationService.getEducationById(education.getId()).get();
        //then
        assertEquals(education, educationFromRepo);
    }

    @Test
    @DisplayName("When get not existed education, " +
            "then must be returned empty optional.")
    public void testGetNotExistedEducation() {
        //when
        Optional<Education> educationOptional =
                educationService.getEducationById(NON_EXISTENT_ID);
        //then
        assertTrue(educationOptional.isEmpty());
    }

    @Test
    @DisplayName("When get educations with specific profileId, " +
            "then return list of educations with this profileId")
    void testGetAllEducationsByProfileID() {
        //when
        List<Education> educationList =
                educationService.getEducationsByProfileID(profile.getId());
        //then
        assertEquals(List.of(education), educationList);
    }

    private Education createEducation() {
        return Education.builder()
                .profile(profile)
                .build();
    }

    private List<Education> getAllEducationsFromRepository() {
        return new CopyOnWriteArrayList<>((CopyOnWriteArrayList<Education>)
                educationRepository.getAll());
    }

}
