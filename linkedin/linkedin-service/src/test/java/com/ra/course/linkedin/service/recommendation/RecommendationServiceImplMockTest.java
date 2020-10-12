package com.ra.course.linkedin.service.recommendation;

import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.model.entity.profile.Recommendation;
import com.ra.course.linkedin.persistence.recommendation.RecommendationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RecommendationServiceImplMockTest {

    private static final long EXISTED_ID = 1L;
    private static final long NON_EXISTENT_ID = -1L;

    private final RecommendationRepository mockRecommendationRepository = mock(RecommendationRepository.class);
    private final RecommendationService recommendationService =
            new RecommendationServiceImpl(mockRecommendationRepository);

    private Recommendation recommendation;

    @BeforeEach
    public void setUp() {
        recommendation = createRecommendation();
    }

    @Test
    @DisplayName("When the recommendation is added or updated, " +
            "then should be called RecommendationRepository's save method")
    public void testAddOrUpdateRecommendation() {
        //when
        recommendationService.addOrUpdateRecommendation(recommendation);
        //then
        verify(mockRecommendationRepository).save(recommendation);
    }

    @Test
    @DisplayName("When the recommendation is deleted, then should be called " +
            "RecommendationRepository's delete method.")
    public void testDeleteRecommendation() {
        //when
        recommendationService.deleteRecommendation(recommendation);
        //then
        verify(mockRecommendationRepository).delete(recommendation);
    }

    @Test
    @DisplayName("When get existed recommendation, it should be returned.")
    public void testGetExistedRecommendation() {
        //when
        when(mockRecommendationRepository.getById(EXISTED_ID))
                .thenReturn(Optional.of(recommendation));
        Optional<Recommendation> result =
                recommendationService.getRecommendationById(
                        recommendation.getId());
        //then
        assertEquals(Optional.of(recommendation), result);
        verify(mockRecommendationRepository).getById(recommendation.getId());
    }

    @Test
    @DisplayName("When get not existed recommendation, " +
            "then must be returned empty optional.")
    public void testGetNotExistedRecommendation() {
        //when
        Optional<Recommendation> result =
                recommendationService.getRecommendationById(NON_EXISTENT_ID);
        //then
        assertEquals(Optional.empty(), result);
        verify(mockRecommendationRepository).getById(NON_EXISTENT_ID);
    }

    @Test
    @DisplayName("When get recommendations with specific profileId, " +
            "then return list of recommendations with this profileId")
    void testGetAllRecommendationsByProfileID() {
        //given
        Recommendation recommendationWithProfileWithExistingId =
                Recommendation.builder()
                        .profile(Profile.builder().id(EXISTED_ID).build())
                        .build();
        //when
        when(recommendationService.getAllRecommendationsByProfileId(EXISTED_ID))
                .thenReturn(List.of(recommendationWithProfileWithExistingId));
        List<Recommendation> recommendationList =
                recommendationService
                        .getAllRecommendationsByProfileId(EXISTED_ID);
        //then
        assertEquals(List.of(recommendationWithProfileWithExistingId),
                recommendationList);
        verify(mockRecommendationRepository).getAllRecommendationsByProfileId(
                recommendationWithProfileWithExistingId.getProfile().getId());
    }

    private Recommendation createRecommendation() {
        return Recommendation.builder()
                .id(EXISTED_ID)
                .build();
    }
}