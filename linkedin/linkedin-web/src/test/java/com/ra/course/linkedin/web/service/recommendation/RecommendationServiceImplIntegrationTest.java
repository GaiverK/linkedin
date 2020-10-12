package com.ra.course.linkedin.web.service.recommendation;

import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.model.entity.profile.Recommendation;
import com.ra.course.linkedin.persistence.profile.ProfileRepository;
import com.ra.course.linkedin.persistence.recommendation.RecommendationRepository;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.service.recommendation.RecommendationService;
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
public class RecommendationServiceImplIntegrationTest {

    private static final long NON_EXISTENT_ID = -1L;
    public static final String NEW_DESCRIPTION = "New description";

    @Autowired
    private RecommendationService recommendationService;
    @Autowired
    private RecommendationRepository recommendationRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileRepository profileRepository;

    private Recommendation recommendation;
    private Profile profile;

    @BeforeEach
    void setUp() {
        profile = new Profile();
        profileService.saveProfile(profile);
        recommendation = createRecommendation();
        recommendationService.addOrUpdateRecommendation(recommendation);
    }

    @AfterEach
    void tearDown() {
        tryToDelete(recommendationRepository, recommendation);
        tryToDelete(profileRepository, profile);
    }

    @Test
    @DisplayName("When the recommendation is added or updated, " +
            "then it should be saved in repository")
    void testAddOrUpdateRecommendation() {
        //then
        final Recommendation recommendationFromRepo =
                recommendationService.getRecommendationById(
                        recommendation.getId()).get();
        assertEquals(recommendationFromRepo, recommendation);
        //when
        recommendation.setDescription(NEW_DESCRIPTION);
        recommendationService.addOrUpdateRecommendation(recommendation);
        //then
        assertEquals(recommendation.getDescription(), NEW_DESCRIPTION);
    }

    @Test
    @DisplayName("When recommendation was deleted, " +
            "then this recommendation should be absent in repository")
    public void testDeleteRecommendation() {
        assertTrue(getAllRecommendationsFromRepository()
                .contains(recommendation));
        //when
        recommendationService.deleteRecommendation(recommendation);
        //then
        assertFalse(getAllRecommendationsFromRepository()
                .contains(recommendation));
    }

    @Test
    @DisplayName("When get existed recommendation, it should be returned.")
    public void testGetExistedRecommendation() {
        //when
        Recommendation recommendationFromRepo = recommendationService
                .getRecommendationById(recommendation.getId()).get();
        //then
        assertEquals(recommendation, recommendationFromRepo);
    }

    @Test
    @DisplayName("When get not existed recommendation, " +
            "then must be returned empty optional.")
    public void testGetNotExistedRecommendation() {
        //when
        Optional<Recommendation> recommendationOptional =
                recommendationService.getRecommendationById(NON_EXISTENT_ID);
        //then
        assertTrue(recommendationOptional.isEmpty());
    }

    @Test
    @DisplayName("When get recommendations with specific profileId, " +
            "then return list of recommendations with this profileId")
    void testGetAllRecommendationsByProfileID() {
        //when
        List<Recommendation> recommendationList = recommendationService
                .getAllRecommendationsByProfileId(profile.getId());
        //then
        assertEquals(List.of(recommendation), recommendationList);
    }

    private Recommendation createRecommendation() {
        return Recommendation.builder()
                .profile(profile)
                .build();
    }

    private List<Recommendation> getAllRecommendationsFromRepository() {
        return new CopyOnWriteArrayList<>((CopyOnWriteArrayList<Recommendation>)
                recommendationRepository.getAll());
    }
}
