package com.ra.course.linkedin.service.utils;

import com.ra.course.linkedin.model.entity.exceptions.EntityNotFoundException;
import com.ra.course.linkedin.model.entity.profile.*;
import com.ra.course.linkedin.persistence.accomplishment.AccomplishmentRepository;
import com.ra.course.linkedin.persistence.education.EducationRepository;
import com.ra.course.linkedin.persistence.experience.ExperienceRepository;
import com.ra.course.linkedin.persistence.profile.ProfileRepository;
import com.ra.course.linkedin.persistence.recommendation.RecommendationRepository;
import com.ra.course.linkedin.persistence.skill.SkillRepository;
import com.ra.course.linkedin.persistence.stats.StatsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProfileUtils {

    private final ProfileRepository profileRepository;
    private final AccomplishmentRepository accomplishmentRep;
    private final EducationRepository educationRepo;
    private final ExperienceRepository experienceRepo;
    private final RecommendationRepository recommendationRep;
    private final SkillRepository skillRepository;
    private final StatsRepository statsRepository;

    public Profile findProfile(final Profile profile) {
        return profileRepository.getById(profile.getId())
                .orElseThrow(()
                        -> new EntityNotFoundException(Profile.class.getSimpleName()));
    }

    public Accomplishment findAccomplishment(final Accomplishment accomplishment) {
        return accomplishmentRep.getById(accomplishment.getId())
                .orElseThrow(()
                        -> new EntityNotFoundException(Accomplishment.class.getSimpleName()));
    }

    public Education findEducation(final Education education) {
        return educationRepo.getById(education.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException(Education.class.getSimpleName()));
    }

    public Experience findExperience(final Experience experience) {
        return experienceRepo.getById(experience.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException(Experience.class.getSimpleName()));
    }

    public Recommendation findRecommendation(final Recommendation recommendation) {
        return recommendationRep.getById(recommendation.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException(Recommendation.class.getSimpleName()));
    }

    public Skill findSkill(final Skill skill) {
        return skillRepository.getById(skill.getId())
                .orElseThrow(() -> new EntityNotFoundException(Skill.class.getSimpleName()));
    }

    public Stats findStats(final Stats stats) {
        return statsRepository.getById(stats.getId())
                .orElseThrow(() -> new EntityNotFoundException(Stats.class.getSimpleName()));
    }
}
