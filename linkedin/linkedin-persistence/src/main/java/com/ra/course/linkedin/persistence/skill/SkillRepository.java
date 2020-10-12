package com.ra.course.linkedin.persistence.skill;

import com.ra.course.linkedin.model.entity.profile.Skill;
import com.ra.course.linkedin.persistence.BaseRepository;

import java.util.List;

public interface SkillRepository extends BaseRepository<Skill> {
    List<Skill> getSkillsByProfileID(long profileId);
}
