package com.ra.course.linkedin.persistence.education;

import com.ra.course.linkedin.model.entity.profile.Education;
import com.ra.course.linkedin.persistence.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class EducationRepositoryImpl extends BaseRepositoryImpl<Education>
        implements EducationRepository {

    @Override
    public List<Education> getEducationsByProfileID(final long profileId) {
        return StreamSupport.stream(getAll().spliterator(), false)
                .filter(education ->
                        education.getProfile().getId() == profileId)
                .collect(Collectors.toList());
    }
}
