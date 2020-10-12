package com.ra.course.linkedin.web.service.profile;

import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.persistence.profile.ProfileRepository;
import com.ra.course.linkedin.service.profile.ProfileService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.ra.course.linkedin.web.service.ServiceIntegrationTestUtils.tryToDelete;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ProfileServiceIntegrationTest {

    public static final String NEW_DESCRIPTION = "New description";

    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileRepository profileRepository;

    private Profile profile;

    @BeforeEach
    void setUp() {
        profile = createProfile();
        profileService.saveProfile(profile);
    }

    @AfterEach
    void tearDown() {
        tryToDelete(profileRepository, profile);
    }

    @Test
    @DisplayName("When get profile by id and it exists in repository, " +
            "then return Optional of this profile")
    public void testGetProfilerByIdWhenItExistsInRepo() {
        //when
        Optional<Profile> profileOptional = profileService.getProfileById(profile.getId());
        //then
        assertEquals(profileOptional, Optional.of(profile));
    }

    @Test
    @DisplayName("When get profile by id and it does not exist in repository," +
            " then must be returned empty optional")
    public void testGetProfilerByIdWhenItDoesNotExistInRepo() {
        //when
        profileRepository.delete(profile);
        Optional<Profile> profileOptional = profileService.getProfileById(profile.getId());
        //then
        assertTrue(profileOptional.isEmpty());
    }

    @Test
    @DisplayName("When the profile is saved and it does not exist " +
            "in repository, then it must be added to the repository")
    void testSaveProfileWhenItDoesNotExistInRepo() {
        //when
        profileRepository.delete(profile);
        Profile result = profileService.saveProfile(profile);
        //then
        assertEquals(result, profile);
    }

    @Test
    @DisplayName("When the profile is saved and it exists in repository, " +
            "then it must be updated in the repository")
    void testSaveProfileWhenItExistsInRepo() {
        //given
        profile.setSummary(NEW_DESCRIPTION);
        //when
        Profile result = profileService.saveProfile(profile);
        //then
        assertEquals(result.getSummary(), NEW_DESCRIPTION);
    }

    @Test
    @DisplayName("When get all profiles, then return List of all profiles")
    void testGetAllProfiles() {
        //then
        assertEquals(profileRepository.getAll(), profileService.getAllProfiles());
    }

    private Profile createProfile() {
        return Profile.builder().build();
    }
}
