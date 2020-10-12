package com.ra.course.linkedin.service.profile;

import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.persistence.profile.ProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    @Override
    public Profile saveProfile(final Profile profile) {
        return profileRepository.save(profile);
    }

    @Override
    public Optional<Profile> getProfileById(final long id) {
        return profileRepository.getById(id);
    }

    @Override
    public List<Profile> getAllProfiles() {
        return StreamSupport.stream(profileRepository.getAll().spliterator(),
                false)
                .collect(Collectors.toList());
    }
}
