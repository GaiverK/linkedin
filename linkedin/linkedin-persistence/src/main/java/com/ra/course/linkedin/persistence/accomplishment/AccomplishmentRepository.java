package com.ra.course.linkedin.persistence.accomplishment;

import com.ra.course.linkedin.model.entity.profile.Accomplishment;
import com.ra.course.linkedin.persistence.BaseRepository;

import java.util.List;

public interface AccomplishmentRepository
        extends BaseRepository<Accomplishment> {
    List<Accomplishment> getAccomplishmentsByProfileID(long profileId);
}
