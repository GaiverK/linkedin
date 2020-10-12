package com.ra.course.linkedin.service.recommendation;

import com.ra.course.linkedin.model.entity.profile.Recommendation;
import com.ra.course.linkedin.persistence.recommendation.RecommendationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository recommendRepo;

    @Override
    public Recommendation addOrUpdateRecommendation(final Recommendation recommendation) {
        return recommendRepo.save(recommendation);
    }

    @Override
    public void deleteRecommendation(final Recommendation recommendation) {
        recommendRepo.delete(recommendation);
    }

    @Override
    public Optional<Recommendation> getRecommendationById(final long id) {
        return recommendRepo.getById(id);
    }

    @Override
    public List<Recommendation> getAllRecommendationsByProfileId(
            final long profileID) {
        return recommendRepo.getAllRecommendationsByProfileId(profileID);
    }
}
