package com.ra.course.linkedin.service.accomplishment;

import com.ra.course.linkedin.model.entity.profile.Accomplishment;
import com.ra.course.linkedin.persistence.accomplishment.AccomplishmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccomplishmentServiceImpl implements AccomplishmentService {

    private final AccomplishmentRepository accomplishRepo;

    @Override
    public Accomplishment addOrUpdateAccomplishment(final Accomplishment accomplishment) {
        return accomplishRepo.save(accomplishment);
    }

    @Override
    public void deleteAccomplishment(final Accomplishment accomplishment) {
        accomplishRepo.delete(accomplishment);
    }

    @Override
    public Optional<Accomplishment> getAccomplishmentById(final long id) {
        return accomplishRepo.getById(id);
    }

    @Override
    public List<Accomplishment> getAccomplishmentsByProfileID(final long profileId) {
        return accomplishRepo.getAccomplishmentsByProfileID(profileId);
    }
}
