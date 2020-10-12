package com.ra.course.linkedin.web.converter;

import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.service.member.MemberService;
import com.ra.course.linkedin.web.dto.profile.ProfileDto;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProfileConverters {
    private final Converter<Profile, Long> dtoIdConverter;
    private final Converter<ProfileDto, Member> profMembConverter;

    public ProfileConverters(final MemberService memberService) {
        dtoIdConverter = context ->
                context.getSource().getMember().getId();
        profMembConverter = context -> memberService.getMemberById(
                context.getSource().getMemberId()).get();
    }

    public Converter<Profile, Long> getDtoIdConverter() {
        return dtoIdConverter;
    }

    public Converter<ProfileDto, Member> getProfMembConverter() {
        return profMembConverter;
    }
}
