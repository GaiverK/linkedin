package com.ra.course.linkedin.service.recommendation;

import com.ra.course.linkedin.model.entity.profile.Recommendation;

import java.util.List;
import java.util.Optional;

public interface RecommendationService {

    Recommendation addOrUpdateRecommendation(Recommendation recommendation);

    void deleteRecommendation(Recommendation recommendation);

    Optional<Recommendation> getRecommendationById(long id);

    List<Recommendation> getAllRecommendationsByProfileId(long profileID);
}
