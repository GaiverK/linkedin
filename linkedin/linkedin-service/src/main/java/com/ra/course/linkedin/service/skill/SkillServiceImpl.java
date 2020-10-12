package com.ra.course.linkedin.service.skill;

import com.ra.course.linkedin.model.entity.profile.Skill;
import com.ra.course.linkedin.persistence.skill.SkillRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SkillServiceImpl implements SkillService{

    private final SkillRepository skillRepository;

    @Override
    public Skill addOrUpdateSkill(final Skill skill) {
        return skillRepository.save(skill);
    }

    @Override
    public void deleteSkill(final Skill skill) {
        skillRepository.delete(skill);
    }

    @Override
    public Optional<Skill> getSkillById(final long id) {
        return skillRepository.getById(id);
    }

    @Override
    public List<Skill> getSkillsByProfileID(final long profileId) {
        return skillRepository.getSkillsByProfileID(profileId);
    }
}
