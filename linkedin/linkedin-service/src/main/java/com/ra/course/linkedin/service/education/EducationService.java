package com.ra.course.linkedin.service.education;

import com.ra.course.linkedin.model.entity.profile.Education;

import java.util.List;
import java.util.Optional;

public interface EducationService {
    Education addOrUpdateEducation(Education education);

    void deleteEducation(Education education);

    Optional<Education> getEducationById(long id);

    List<Education> getEducationsByProfileID(long profileId);
}
