package com.ra.course.linkedin.persistence.accomplishment;

import com.ra.course.linkedin.model.entity.profile.Accomplishment;
import com.ra.course.linkedin.persistence.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class AccomplishmentRepositoryImpl
        extends BaseRepositoryImpl<Accomplishment>
        implements AccomplishmentRepository{

    @Override
    public List<Accomplishment> getAccomplishmentsByProfileID(final long profileId) {
        return StreamSupport.stream(getAll().spliterator(), false)
                .filter(accomplishment ->
                        accomplishment.getProfile().getId() == profileId)
                .collect(Collectors.toList());
    }
}
