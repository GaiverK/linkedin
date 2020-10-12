package com.ra.course.linkedin.web.mapper;

import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.web.converter.ProfileConverters;
import com.ra.course.linkedin.web.dto.profile.ProfileDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.modelmapper.builder.ConfigurableMapExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapperUtil {

    private transient final TypeMap<Profile, ProfileDto> profileDtoTypeMap;
    private transient final TypeMap<ProfileDto, Profile> profileTypeMap;

    @Autowired
    public ProfileMapperUtil(final ModelMapper modelMapper,
                             final ProfileConverters profileConverters) {
        profileDtoTypeMap = modelMapper.createTypeMap(Profile.class, ProfileDto.class);
        profileTypeMap = modelMapper.createTypeMap(ProfileDto.class, Profile.class);

        final PropertyMap<Profile, ProfileDto> personMap = new PropertyMap<>() {
            protected void configure() {
                map().getMember().setProfileId(source.getId());
            }
        };

        modelMapper.addMappings(personMap);

        profileDtoTypeMap.addMappings(
                (ConfigurableMapExpression<Profile, ProfileDto> mapping) ->
                        mapping.using(profileConverters.getDtoIdConverter()).map(
                                memberId -> memberId, ProfileDto::setMemberId));
        profileTypeMap.addMappings(
                (ConfigurableMapExpression<ProfileDto, Profile> mapping) ->
                        mapping.using(profileConverters.getProfMembConverter()).map(
                                member -> member, Profile::setMember));
    }

    public ProfileDto mapFromProfileToDto(final Profile profile) {
        return profileDtoTypeMap.map(profile);
    }

    public Profile mapFromDtoToProfile(final ProfileDto profileDto) {
        return profileTypeMap.map(profileDto);
    }
}
