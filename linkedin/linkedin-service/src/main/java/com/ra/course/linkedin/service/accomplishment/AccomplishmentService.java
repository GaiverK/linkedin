package com.ra.course.linkedin.service.accomplishment;

import com.ra.course.linkedin.model.entity.profile.Accomplishment;

import java.util.List;
import java.util.Optional;

public interface AccomplishmentService {

    Accomplishment addOrUpdateAccomplishment(Accomplishment accomplishment);

    void deleteAccomplishment(Accomplishment accomplishment);

    Optional<Accomplishment> getAccomplishmentById(long id);

    List<Accomplishment> getAccomplishmentsByProfileID(long profileId);
}
