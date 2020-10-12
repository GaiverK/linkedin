package com.ra.course.linkedin.service.education;

import com.ra.course.linkedin.model.entity.profile.Education;
import com.ra.course.linkedin.persistence.education.EducationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepo;

    @Override
    public Education addOrUpdateEducation(final Education education) {
        return educationRepo.save(education);
    }

    @Override
    public void deleteEducation(final Education education) {
        educationRepo.delete(education);
    }

    @Override
    public Optional<Education> getEducationById(final long id) {
        return educationRepo.getById(id);
    }

    @Override
    public List<Education> getEducationsByProfileID(final long profileId) {
        return educationRepo.getEducationsByProfileID(profileId);
    }
}
