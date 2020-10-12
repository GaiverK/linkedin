package com.ra.course.linkedin.service.profile;

import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.persistence.profile.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProfileServiceImplMockTest {

    private static final long EXISTED_ID = 1L;
    private static final long NON_EXISTENT_ID = -1L;

    private final ProfileRepository mockProfileRepository = mock(ProfileRepository.class);
    private final ProfileService profileService = new ProfileServiceImpl(
            mockProfileRepository);

    private Profile profile;

    @BeforeEach
    public void setUp() {
        profile = createProfile();
    }

    @Test
    @DisplayName("When get profile by id and it exists in repository, " +
            "then return Optional of this profile")
    public void testGetProfilerByIdWhenItExistsInRepo() {
        //when
        when(profileService.getProfileById(EXISTED_ID))
                .thenReturn(Optional.of(profile));
        Optional<Profile> result = profileService.getProfileById(profile.getId());
        //then
        assertEquals(result, Optional.of(profile));
        verify(mockProfileRepository).getById(EXISTED_ID);
    }

    @Test
    @DisplayName("When get profile by id and it does not exist in repository, " +
            "then must be returned empty optional")
    public void testGetProfilerByIdWhenItDoesNotExistInRepo() {
        //when
        Optional<Profile> profileOptional =
                profileService.getProfileById(NON_EXISTENT_ID);
        //then
        assertEquals(profileOptional, Optional.empty());
        verify(mockProfileRepository).getById(NON_EXISTENT_ID);
    }

    @Test
    @DisplayName("When profile is saved, then should be called " +
            "ProfileRepository's save method")
    public void testSaveProfile() {
        //when
        profileService.saveProfile(profile);
        //then
        verify(mockProfileRepository).save(profile);
    }

    @Test
    @DisplayName("When get all profiles, then return list of all profiles")
    void testGetAllProfiles() {
        //when
        when(mockProfileRepository.getAll()).thenReturn(List.of(profile));
        List<Profile> allProfiles = profileService.getAllProfiles();
        //then
        assertEquals(allProfiles, List.of(profile));
        verify(mockProfileRepository).getAll();
    }

    private Profile createProfile() {
        return Profile.builder()
                .id(EXISTED_ID)
                .build();
    }
}