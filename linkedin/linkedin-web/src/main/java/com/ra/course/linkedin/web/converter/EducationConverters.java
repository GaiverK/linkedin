package com.ra.course.linkedin.web.converter;

import com.ra.course.linkedin.model.entity.profile.Education;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.web.dto.education.EducationDto;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;

@Component
public class EducationConverters {

    private final Converter<Education, String> dtoNameConverter;
    private final Converter<EducationDto, Profile> educProfConverter;

    public EducationConverters(final ProfileService profileService) {
        dtoNameConverter = context ->
                context.getSource().getProfile().getMember().getName();
        educProfConverter = context -> profileService.getProfileById(
                context.getSource().getProfileId()).get();
    }

    public Converter<Education, String> getDtoNameConverter() {
        return dtoNameConverter;
    }

    public Converter<EducationDto, Profile> getEducProfConverter() {
        return educProfConverter;
    }
}
