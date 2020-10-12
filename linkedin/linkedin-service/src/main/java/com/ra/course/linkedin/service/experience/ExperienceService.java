package com.ra.course.linkedin.service.experience;

import com.ra.course.linkedin.model.entity.profile.Experience;

import java.util.List;
import java.util.Optional;

public interface ExperienceService {
    Experience addOrUpdateExperience(Experience experience);

    void deleteExperience(Experience experience);

    Optional<Experience> getExperienceById(long id);

    List<Experience> getExperiencesByProfileID(long profileId);
}
