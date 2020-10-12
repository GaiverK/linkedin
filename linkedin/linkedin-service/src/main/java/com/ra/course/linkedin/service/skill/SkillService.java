package com.ra.course.linkedin.service.skill;

import com.ra.course.linkedin.model.entity.profile.Skill;

import java.util.List;
import java.util.Optional;

public interface SkillService {

    Skill addOrUpdateSkill(Skill skill);

    void deleteSkill(Skill skill);

    Optional<Skill> getSkillById(long id);

    List<Skill> getSkillsByProfileID(long profileId);
}
