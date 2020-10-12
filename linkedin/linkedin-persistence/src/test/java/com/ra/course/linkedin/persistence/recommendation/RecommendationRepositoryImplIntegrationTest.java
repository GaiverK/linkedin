package com.ra.course.linkedin.persistence.recommendation;

import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.model.entity.profile.Recommendation;
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
public class RecommendationRepositoryImplIntegrationTest {

    private static final long NON_EXISTENT_ID = -1L;

    @Autowired
    RecommendationRepository recommendationRepository;

    @Autowired
    ProfileRepository profileRepository;

    private Profile profile;

    private List<Recommendation> searchedRecommendations;

    @BeforeEach
    public void setUp() {
        profile = new Profile();
        profileRepository.save(profile);
        searchedRecommendations = createSearchedRecommendations();
        searchedRecommendations.forEach(recommendationRepository::save);
    }

    @AfterEach
    public void tearDown() {
        profileRepository.delete(profile);
        searchedRecommendations.forEach(recommendationRepository::delete);
    }

    @Test
    @DisplayName("When searched recommendations exist, " +
            "then must be returned their list")
    void testWhenSearchedRecommendationsExist() {
        //when
        List<Recommendation> recommendationsByProfileId =
                recommendationRepository.getAllRecommendationsByProfileId(
                        profile.getId());
        //then
        assertAll(
                () -> assertEquals(recommendationsByProfileId.size(), 3),
                () -> assertEquals(recommendationsByProfileId.get(0).getDescription(),
                        createSearchedRecommendations().get(0).getDescription()),
                () -> assertEquals(recommendationsByProfileId.get(1).getDescription(),
                        createSearchedRecommendations().get(1).getDescription()),
                () -> assertEquals(recommendationsByProfileId.get(2).getDescription(),
                        createSearchedRecommendations().get(2).getDescription())
        );
    }

    @Test
    @DisplayName("When searched recommendations do not exist, " +
            "then must be returned empty list")
    void testWhenSearchedRecommendationsDoNotExist() {
        //when
        List<Recommendation> recommendationsByProfileId =
                recommendationRepository.getAllRecommendationsByProfileId(
                        NON_EXISTENT_ID);
        //then
        assertEquals(recommendationsByProfileId.size(), 0);
    }

    private List<Recommendation> createSearchedRecommendations() {
        List<Recommendation> searchedRecommendations = new ArrayList<>();
        Recommendation recommendation = Recommendation.builder()
                .profile(profile)
                .description("recommendation")
                .build();
        Recommendation recommendation1 = Recommendation.builder()
                .profile(profile)
                .description("recommendation1")
                .build();
        Recommendation recommendation2 = Recommendation.builder()
                .profile(profile)
                .description("recommendation2")
                .build();
        searchedRecommendations.add(recommendation);
        searchedRecommendations.add(recommendation1);
        searchedRecommendations.add(recommendation2);

        return searchedRecommendations;
    }
}
