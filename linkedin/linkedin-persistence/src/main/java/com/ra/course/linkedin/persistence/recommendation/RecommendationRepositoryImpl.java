package com.ra.course.linkedin.persistence.recommendation;

import com.ra.course.linkedin.model.entity.profile.Recommendation;
import com.ra.course.linkedin.persistence.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class RecommendationRepositoryImpl
        extends BaseRepositoryImpl<Recommendation>
        implements RecommendationRepository {
    @Override
    public List<Recommendation> getAllRecommendationsByProfileId(
            final long profileID) {
        return StreamSupport.stream(getAll().spliterator(), false)
                .filter(recommendation ->
                        recommendation.getProfile().getId() == profileID)
                .collect(Collectors.toList());
    }
}
