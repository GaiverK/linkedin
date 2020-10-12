package com.ra.course.linkedin.web.converter;

import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.model.entity.profile.Skill;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.web.dto.skill.SkillDto;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;

@Component
public class SkillConverters {

     private final Converter<Skill, String> dtoNameConverter;
     private final Converter<SkillDto, Profile> skilProfConverter;

    public SkillConverters(final ProfileService profileService) {
        dtoNameConverter= context ->
                context.getSource().getProfile().getMember().getName();
        skilProfConverter = context ->
                profileService.getProfileById(context.getSource().getProfileId()).get();
    }

    public Converter<Skill, String> getDtoNameConverter() {
        return dtoNameConverter;
    }

    public Converter<SkillDto, Profile> getSkilProfConverter() {
        return skilProfConverter;
    }
}
