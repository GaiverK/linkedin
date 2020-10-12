package com.ra.course.linkedin.persistence.skill;

import com.ra.course.linkedin.model.entity.profile.Skill;
import com.ra.course.linkedin.persistence.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class SkillRepositoryImpl extends BaseRepositoryImpl<Skill>
        implements SkillRepository {

    @Override
    public List<Skill> getSkillsByProfileID(final long profileId) {
        return StreamSupport.stream(getAll().spliterator(), false)
                .filter(skill -> skill.getProfile().getId() == profileId)
                .collect(Collectors.toList());
    }
}
