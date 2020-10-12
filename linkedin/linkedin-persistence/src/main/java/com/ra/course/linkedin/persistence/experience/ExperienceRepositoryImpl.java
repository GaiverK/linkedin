package com.ra.course.linkedin.persistence.experience;

import com.ra.course.linkedin.model.entity.profile.Experience;
import com.ra.course.linkedin.persistence.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class ExperienceRepositoryImpl extends BaseRepositoryImpl<Experience>
        implements ExperienceRepository {

    @Override
    public List<Experience> getExperiencesByProfileID(final long profileId) {
        return StreamSupport.stream(getAll().spliterator(), false)
                .filter(experience ->
                        experience.getProfile().getId() == profileId)
                .collect(Collectors.toList());
    }
}
