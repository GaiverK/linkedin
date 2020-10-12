package com.ra.course.linkedin.web.mapper;

import com.ra.course.linkedin.model.entity.profile.Education;
import com.ra.course.linkedin.web.converter.EducationConverters;
import com.ra.course.linkedin.web.dto.education.EducationDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.builder.ConfigurableMapExpression;
import org.springframework.stereotype.Component;

import static org.modelmapper.convention.MatchingStrategies.STANDARD;

@Component
public class EducationMapperUtil {

    private transient final TypeMap<Education, EducationDto> educatDtoTypeMap;
    private transient final TypeMap<EducationDto, Education> educationTypeMap;

    public EducationMapperUtil(final ModelMapper modelMapper, final EducationConverters educatConverters) {
        modelMapper.getConfiguration().setMatchingStrategy(STANDARD);
        educatDtoTypeMap = modelMapper
                .createTypeMap(Education.class, EducationDto.class);
        educationTypeMap = modelMapper
                .createTypeMap(EducationDto.class, Education.class);
        educatDtoTypeMap.addMappings(
                (ConfigurableMapExpression<Education, EducationDto> mapping) -> {
                    mapping.using(educatConverters.getDtoNameConverter())
                            .map(memberName -> memberName,
                                    EducationDto::setMemberName);
                });
        educationTypeMap.addMappings(
                (ConfigurableMapExpression<EducationDto, Education> mapping) -> {
                    mapping.using(educatConverters.getEducProfConverter()).map(
                            profile -> profile, Education::setProfile);
                });
    }

    public EducationDto mapFromEducationToDto(final Education education) {
        return educatDtoTypeMap.map(education);
    }

    public Education mapFromDtoToEducation(final EducationDto educationDto) {
        return educationTypeMap.map(educationDto);
    }

}
