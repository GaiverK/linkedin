package com.ra.course.linkedin.web.mapper;

import com.ra.course.linkedin.model.entity.profile.Experience;
import com.ra.course.linkedin.web.converter.ExperienceConverters;
import com.ra.course.linkedin.web.dto.experience.ExperienceDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.builder.ConfigurableMapExpression;
import org.springframework.stereotype.Component;

import static org.modelmapper.convention.MatchingStrategies.STANDARD;

@Component
public class ExperienceMapperUtil {

    private transient final TypeMap<Experience, ExperienceDto> experDtoTypeMap;
    private transient final TypeMap<ExperienceDto, Experience> experienceTypeMap;

    public ExperienceMapperUtil(final ModelMapper modelMapper,
                                final ExperienceConverters experConverters) {
        modelMapper.getConfiguration().setMatchingStrategy(STANDARD);
        experDtoTypeMap = modelMapper.createTypeMap(Experience.class,
                ExperienceDto.class);
        experienceTypeMap = modelMapper.createTypeMap(
                ExperienceDto.class, Experience.class);

        experDtoTypeMap.addMappings(
                (ConfigurableMapExpression<Experience, ExperienceDto> mapping) -> {
                    mapping.using(experConverters.getDtoFromConverter()).map(
                            from -> from, ExperienceDto::setFrom);
                    mapping.using(experConverters.getDtoToConverter()).map(
                            to -> to, ExperienceDto::setTo);
                    mapping.using(experConverters.getDtoNameConverter()).map(
                            memberName -> memberName, ExperienceDto::setMemberName);
                });
        experienceTypeMap.addMappings(
                (ConfigurableMapExpression<ExperienceDto, Experience> mapping) -> {
                    mapping.using(experConverters.getExpProfConverter()).map(
                            profile -> profile, Experience::setProfile);
                    mapping.using(experConverters.getExpFromConverter()).map(
                            from -> from, Experience::setFrom);
                    mapping.using(experConverters.getExpToConverter()).map(
                            to -> to, Experience::setTo);
                });
    }

    public ExperienceDto mapFromExperienceToDto(final Experience experience) {
        return experDtoTypeMap.map(experience);
    }

    public Experience mapFromDtoToExperience(final ExperienceDto experienceDto) {
        return experienceTypeMap.map(experienceDto);
    }
}
