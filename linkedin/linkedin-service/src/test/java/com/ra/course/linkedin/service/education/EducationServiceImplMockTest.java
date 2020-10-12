package com.ra.course.linkedin.service.education;

import com.ra.course.linkedin.model.entity.profile.Education;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.persistence.education.EducationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EducationServiceImplMockTest {

    private static final long EXISTED_ID = 1L;
    private static final long NON_EXISTENT_ID = -1L;

    private final EducationRepository mockEducationRepository = mock(EducationRepository.class);
    private final EducationService educationService =
            new EducationServiceImpl(mockEducationRepository);

    private Education education;

    @BeforeEach
    public void setUp() {
        education = createEducation();
    }

    @Test
    @DisplayName("When the education is added or updated, " +
            "then should be called EducationRepository's save method")
    public void testAddOrUpdateEducation() {
        //given
        Education education = new Education();
        //when
        educationService.addOrUpdateEducation(education);
        //then
        verify(mockEducationRepository).save(education);
    }

    @Test
    @DisplayName("When the education is deleted, then should be called " +
            "EducationRepository's delete method.")
    public void testDeleteEducation() {
        //when
        educationService.deleteEducation(education);
        //then
        verify(mockEducationRepository).delete(education);
    }

    @Test
    @DisplayName("When get existed education, it should be returned.")
    public void testGetExistedEducation() {
        //when
        when(mockEducationRepository.getById(EXISTED_ID))
                .thenReturn(Optional.of(education));
        Optional<Education> result = educationService.getEducationById(
                education.getId());
        //then
        assertEquals(Optional.of(education), result);
        verify(mockEducationRepository).getById(education.getId());
    }

    @Test
    @DisplayName("When get not existed education, " +
            "then must be returned empty optional.")
    public void testGetNotExistedEducation() {
        //when
        Optional<Education> educationOptional =
                educationService.getEducationById(NON_EXISTENT_ID);
        //then
        assertEquals(Optional.empty(), educationOptional);
        verify(mockEducationRepository).getById(NON_EXISTENT_ID);
    }

    @Test
    @DisplayName("When get educations with specific profileId, " +
            "then return list of educations with this profileId")
    void testGetAllEducationsByProfileID() {
        //given
        Education educationWithProfileWithExistingId =
                Education.builder()
                        .profile(Profile.builder()
                                .id(EXISTED_ID).build())
                        .build();
        //when
        when(educationService.getEducationsByProfileID(EXISTED_ID))
                .thenReturn(List.of(educationWithProfileWithExistingId));
        List<Education> educationList =
                educationService.getEducationsByProfileID(EXISTED_ID);
        //then
        assertEquals(List.of(educationWithProfileWithExistingId),
                educationList);
        verify(mockEducationRepository).getEducationsByProfileID(
                educationWithProfileWithExistingId.getProfile().getId());
    }

    private Education createEducation() {
        return Education.builder()
                .id(EXISTED_ID)
                .build();
    }
}