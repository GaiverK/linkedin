package com.ra.course.linkedin.persistence.recommendation;

import com.ra.course.linkedin.model.entity.profile.Recommendation;
import com.ra.course.linkedin.persistence.BaseRepository;

import java.util.List;

public interface RecommendationRepository extends BaseRepository<Recommendation> {

    List<Recommendation> getAllRecommendationsByProfileId(long profileID);
}
