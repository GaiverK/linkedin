package com.ra.course.linkedin.persistence.education;

import com.ra.course.linkedin.model.entity.profile.Education;
import com.ra.course.linkedin.persistence.BaseRepository;

import java.util.List;

public interface EducationRepository extends BaseRepository<Education> {

    List<Education> getEducationsByProfileID(long profileId);
}
