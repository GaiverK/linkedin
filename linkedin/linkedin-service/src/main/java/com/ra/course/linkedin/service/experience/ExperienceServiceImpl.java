package com.ra.course.linkedin.service.experience;

import com.ra.course.linkedin.model.entity.profile.Experience;
import com.ra.course.linkedin.persistence.experience.ExperienceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository experienceRepo;

    @Override
    public Experience addOrUpdateExperience(final Experience experience) {
        return experienceRepo.save(experience);
    }

    @Override
    public void deleteExperience(final Experience experience) {
        experienceRepo.delete(experience);
    }

    @Override
    public Optional<Experience> getExperienceById(final long id) {
        return experienceRepo.getById(id);
    }

    @Override
    public List<Experience> getExperiencesByProfileID(final long profileId) {
        return experienceRepo.getExperiencesByProfileID(profileId);
    }
}
