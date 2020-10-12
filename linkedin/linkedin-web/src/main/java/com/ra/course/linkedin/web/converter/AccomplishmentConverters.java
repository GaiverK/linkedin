package com.ra.course.linkedin.web.converter;

import com.ra.course.linkedin.model.entity.profile.Accomplishment;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.web.dto.accomplishment.AccomplishmentDto;
import com.ra.course.linkedin.web.utils.ConverterUtils;
import org.modelmapper.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AccomplishmentConverters {

    @Autowired
    private transient ConverterUtils converterUtils;

    private final Converter<Accomplishment, String> dtoNameConverter;
    private final Converter<AccomplishmentDto, Profile> accProfConverter;
    private final Converter<AccomplishmentDto, LocalDate> accDateConverter;
    private final Converter<Accomplishment, String> dtoDateConverter;

    public AccomplishmentConverters(final ProfileService profileService) {
        dtoNameConverter = context ->
                context.getSource().getProfile().getMember().getName();
        accProfConverter = context ->
                profileService.getProfileById(context.getSource().getProfileId()).get();
        accDateConverter = context ->
                converterUtils.formattedDate(context.getSource().getDate());
        dtoDateConverter = context ->
                converterUtils.formattedDate(context.getSource().getDate());
    }

    public Converter<Accomplishment, String> getDtoNameConverter() {
        return dtoNameConverter;
    }

    public Converter<AccomplishmentDto, Profile> getAccProfConverter() {
        return accProfConverter;
    }

    public Converter<AccomplishmentDto, LocalDate> getAccDateConverter() {
        return accDateConverter;
    }

    public Converter<Accomplishment, String> getDtoDateConverter() {
        return dtoDateConverter;
    }
}
