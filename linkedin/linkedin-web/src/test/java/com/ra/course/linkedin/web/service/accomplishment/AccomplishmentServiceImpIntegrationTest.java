package com.ra.course.linkedin.web.service.accomplishment;

import com.ra.course.linkedin.model.entity.profile.Accomplishment;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.persistence.accomplishment.AccomplishmentRepository;
import com.ra.course.linkedin.persistence.profile.ProfileRepository;
import com.ra.course.linkedin.service.accomplishment.AccomplishmentService;
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
public class AccomplishmentServiceImpIntegrationTest {

    private static final long NON_EXISTENT_ID = -1L;
    public static final String NEW_DESCRIPTION = "New description";

    @Autowired
    private AccomplishmentService accomplishmentService;
    @Autowired
    private AccomplishmentRepository accomplishmentRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileRepository profileRepository;

    private Accomplishment accomplishment;
    private Profile profile;

    @BeforeEach
    void setUp() {
        profile = new Profile();
        profileService.saveProfile(profile);
        accomplishment = createAccomplishment();
        accomplishmentService.addOrUpdateAccomplishment(accomplishment);
    }

    @AfterEach
    void tearDown() {
        tryToDelete(accomplishmentRepository, accomplishment);
        tryToDelete(profileRepository, profile);
    }

    @Test
    @DisplayName("When the accomplishment is added or updated, " +
            "then it should be saved in repository")
    void testAddOrUpdateAccomplishment() {
        //then
        final Accomplishment accomplishmentFromRepo =
                accomplishmentService.getAccomplishmentById(
                        accomplishment.getId()).get();
        assertEquals(accomplishmentFromRepo, accomplishment);
        //when
        accomplishment.setDescription(NEW_DESCRIPTION);
        accomplishmentService.addOrUpdateAccomplishment(accomplishment);
        //then
        assertEquals(accomplishment.getDescription(), NEW_DESCRIPTION);
    }

    @Test
    @DisplayName("When accomplishment was deleted, " +
            "then this accomplishment should be absent in repository")
    public void testDeleteAccomplishment() {
        assertTrue(getAllAccomplishmentsFromRepository()
                .contains(accomplishment));
        //when
        accomplishmentService.deleteAccomplishment(accomplishment);
        //then
        assertFalse(getAllAccomplishmentsFromRepository()
                .contains(accomplishment));
    }

    @Test
    @DisplayName("When get existed accomplishment, it should be returned.")
    public void testGetExistedAccomplishment() {
        //when
        Accomplishment accomplishmentFromRepo =
                accomplishmentService.getAccomplishmentById(
                        accomplishment.getId()).get();
        //then
        assertEquals(accomplishment, accomplishmentFromRepo);
    }

    @Test
    @DisplayName("When get not existed accomplishment, " +
            "then must be returned empty optional.")
    public void testGetNotExistedAccomplishment() {
        //when
        Optional<Accomplishment> accomplishmentOptional =
                accomplishmentService.getAccomplishmentById(NON_EXISTENT_ID);
        //then
        assertTrue(accomplishmentOptional.isEmpty());
    }

    @Test
    @DisplayName("When get accomplishment with specific profileId, " +
            "then return list of accomplishments with this profileId")
    public void testGetAllAccomplishmentsByProfileID() {
        //when
        List<Accomplishment> accomplishmentList = accomplishmentService
                .getAccomplishmentsByProfileID(profile.getId());
        //then
        assertEquals(List.of(accomplishment), accomplishmentList);
    }

    private Accomplishment createAccomplishment() {
        return Accomplishment.builder()
                .profile(profile)
                .build();
    }

    private List<Accomplishment> getAllAccomplishmentsFromRepository() {
        return new CopyOnWriteArrayList<>(
                (CopyOnWriteArrayList<Accomplishment>)
                        accomplishmentRepository.getAll());
    }
}
