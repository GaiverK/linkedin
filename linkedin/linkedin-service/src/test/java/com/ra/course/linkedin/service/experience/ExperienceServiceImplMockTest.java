package com.ra.course.linkedin.service.experience;

import com.ra.course.linkedin.model.entity.profile.Experience;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.persistence.experience.ExperienceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ExperienceServiceImplMockTest {

    private static final long EXISTED_ID = 1L;
    private static final long NON_EXISTENT_ID = -1L;

    private final ExperienceRepository mockExperienceRepository = mock(ExperienceRepository.class);
    private final ExperienceService experienceService =
            new ExperienceServiceImpl(mockExperienceRepository);

    private Experience experience;

    @BeforeEach
    public void setUp() {
        experience = createExperience();
    }

    @Test
    @DisplayName("When the experience is added or updated, " +
            "then should be called ExperienceRepository's save method")
    public void testAddOrUpdateExperience() {
        //when
        experienceService.addOrUpdateExperience(experience);
        //then
        verify(mockExperienceRepository).save(experience);
    }

    @Test
    @DisplayName("When the experience is deleted, then should be called " +
            "ExperienceRepository's delete method.")
    public void testDeleteExperience() {
        //when
        experienceService.deleteExperience(experience);
        //then
        verify(mockExperienceRepository).delete(experience);
    }

    @Test
    @DisplayName("When get existed experience, it should be returned.")
    public void testGetExistedExperience() {
        //when
        when(mockExperienceRepository.getById(EXISTED_ID))
                .thenReturn(Optional.of(experience));
        Optional<Experience> result = experienceService.getExperienceById(
                experience.getId());
        //then
        assertEquals(Optional.of(experience), result);
        verify(mockExperienceRepository).getById(experience.getId());
    }

    @Test
    @DisplayName("When get not existed experience, " +
            "then must be returned empty optional.")
    public void testGetNotExistedExperience() {
        //when
        Optional<Experience> experienceOptional =
                experienceService.getExperienceById(NON_EXISTENT_ID);
        //then
        assertEquals(Optional.empty(), experienceOptional);
        verify(mockExperienceRepository).getById(NON_EXISTENT_ID);
    }

    @Test
    @DisplayName("When get experiences with specific profileId, " +
            "then return list of experiences with this profileId")
    void testGetAllExperiencesByProfileID() {
        //given
        Experience experienceWithProfileWithExistingId =
                Experience.builder()
                        .profile(Profile.builder().id(EXISTED_ID).build())
                        .build();
        //when
        when(experienceService.getExperiencesByProfileID(EXISTED_ID))
                .thenReturn(List.of(experienceWithProfileWithExistingId));
        List<Experience> experienceList =
                experienceService.getExperiencesByProfileID(EXISTED_ID);
        //then
        assertEquals(List.of(experienceWithProfileWithExistingId),
                experienceList);
        verify(mockExperienceRepository).getExperiencesByProfileID(
                experienceWithProfileWithExistingId.getProfile().getId());
    }

    private Experience createExperience() {
        return Experience.builder()
                .id(EXISTED_ID)
                .build();
    }
}
