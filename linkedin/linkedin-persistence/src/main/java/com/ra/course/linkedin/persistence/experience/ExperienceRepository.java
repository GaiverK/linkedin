package com.ra.course.linkedin.persistence.experience;

import com.ra.course.linkedin.model.entity.profile.Experience;
import com.ra.course.linkedin.persistence.BaseRepository;

import java.util.List;

public interface ExperienceRepository extends BaseRepository<Experience> {
    List<Experience> getExperiencesByProfileID(long profileId);
}
