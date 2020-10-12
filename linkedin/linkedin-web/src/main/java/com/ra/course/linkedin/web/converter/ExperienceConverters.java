package com.ra.course.linkedin.web.converter;

import com.ra.course.linkedin.model.entity.profile.Experience;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.web.dto.experience.ExperienceDto;
import com.ra.course.linkedin.web.utils.ConverterUtils;
import org.modelmapper.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ExperienceConverters {

    @Autowired
    private transient ConverterUtils converterUtils;

    private final Converter<Experience, String> dtoNameConverter;
    private final Converter<ExperienceDto, Profile> expProfConverter;
    private final Converter<ExperienceDto, LocalDate> expFromConverter;
    private final Converter<ExperienceDto, LocalDate> expToConverter;
    private final Converter<Experience, String> dtoFromConverter;
    private final Converter<Experience, String> dtoToConverter;

    public ExperienceConverters(final ProfileService profileService) {
        dtoNameConverter = context ->
                context.getSource().getProfile().getMember().getName();
        expProfConverter = context ->
                profileService.getProfileById(context.getSource().getProfileId()).get();
        expFromConverter = context ->
                converterUtils.formattedDate(context.getSource().getFrom());
        expToConverter = context ->
                converterUtils.formattedDate(context.getSource().getTo());
        dtoFromConverter = context ->
                converterUtils.formattedDate(context.getSource().getFrom());
        dtoToConverter = context ->
                converterUtils.formattedDate(context.getSource().getTo());
    }

    public Converter<Experience, String> getDtoNameConverter() {
        return dtoNameConverter;
    }

    public Converter<ExperienceDto, Profile> getExpProfConverter() {
        return expProfConverter;
    }

    public Converter<ExperienceDto, LocalDate> getExpFromConverter() {
        return expFromConverter;
    }

    public Converter<ExperienceDto, LocalDate> getExpToConverter() {
        return expToConverter;
    }

    public Converter<Experience, String> getDtoFromConverter() {
        return dtoFromConverter;
    }

    public Converter<Experience, String> getDtoToConverter() {
        return dtoToConverter;
    }
}
