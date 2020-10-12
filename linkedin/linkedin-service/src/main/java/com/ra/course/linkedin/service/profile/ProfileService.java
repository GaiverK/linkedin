package com.ra.course.linkedin.service.profile;

import com.ra.course.linkedin.model.entity.profile.Profile;

import java.util.List;
import java.util.Optional;

public interface ProfileService {

    Profile saveProfile(Profile profile);

    Optional<Profile> getProfileById(long id);

    List<Profile> getAllProfiles();
}
