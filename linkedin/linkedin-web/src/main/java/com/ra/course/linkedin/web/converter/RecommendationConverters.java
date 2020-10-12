package com.ra.course.linkedin.web.converter;

import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.model.entity.profile.Recommendation;
import com.ra.course.linkedin.service.member.MemberService;
import com.ra.course.linkedin.service.profile.ProfileService;
import com.ra.course.linkedin.web.dto.recommendation.RecommendationDto;
import com.ra.course.linkedin.web.utils.ConverterUtils;
import org.modelmapper.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RecommendationConverters {

    @Autowired
    private transient ConverterUtils converterUtils;

    private final Converter<Recommendation, String> dtoNameConverter;
    private final Converter<RecommendationDto, Profile> recProfConverter;
    private final Converter<RecommendationDto, LocalDate> recDateConverter;
    private final Converter<Recommendation, String> dtoDateConverter;
    private final Converter<RecommendationDto, Member> recAuthConverter;
    private final Converter<Recommendation, Long> dtoIdConverter;

    public RecommendationConverters(final ProfileService profileService,
                                    final MemberService memberService) {
        dtoNameConverter = context ->
                context.getSource().getProfile().getMember().getName();
        recProfConverter = context ->
                profileService.getProfileById(context.getSource().getProfileId()).get();
        recDateConverter = context -> (context.getSource().getCreatedOn() == null) ?
                LocalDate.now() :
                converterUtils.formattedDate(context.getSource().getCreatedOn());
        dtoDateConverter = context ->
                converterUtils.formattedDate(context.getSource().getCreatedOn());
        recAuthConverter = context -> memberService.getMemberById(
                context.getSource().getAuthorId()).get();
        dtoIdConverter = context ->
                context.getSource().getAuthor().getId();
    }

    public Converter<Recommendation, String> getDtoNameConverter() {
        return dtoNameConverter;
    }

    public Converter<RecommendationDto, Profile> getRecProfConverter() {
        return recProfConverter;
    }

    public Converter<RecommendationDto, LocalDate> getRecDateConverter() {
        return recDateConverter;
    }

    public Converter<Recommendation, String> getDtoDateConverter() {
        return dtoDateConverter;
    }

    public Converter<RecommendationDto, Member> getRecAuthConverter() {
        return recAuthConverter;
    }

    public Converter<Recommendation, Long> getDtoIdConverter() {
        return dtoIdConverter;
    }
}
