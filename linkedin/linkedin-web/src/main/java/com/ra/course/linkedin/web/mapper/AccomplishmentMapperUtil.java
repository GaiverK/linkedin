package com.ra.course.linkedin.web.mapper;

import com.ra.course.linkedin.model.entity.profile.Accomplishment;
import com.ra.course.linkedin.web.converter.AccomplishmentConverters;
import com.ra.course.linkedin.web.dto.accomplishment.AccomplishmentDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.builder.ConfigurableMapExpression;
import org.springframework.stereotype.Component;

import static org.modelmapper.convention.MatchingStrategies.STANDARD;

@Component
public class AccomplishmentMapperUtil {

    private final transient TypeMap<Accomplishment, AccomplishmentDto>
            accomplDtoTypeMap;
    private final transient TypeMap<AccomplishmentDto, Accomplishment>
            accomplTypeMap;

    public AccomplishmentMapperUtil(final ModelMapper modelMapper,
                                    final AccomplishmentConverters accomplConverters) {
        modelMapper.getConfiguration().setMatchingStrategy(STANDARD);
        accomplDtoTypeMap = modelMapper.createTypeMap(
                Accomplishment.class, AccomplishmentDto.class);
        accomplTypeMap = modelMapper.createTypeMap(
                AccomplishmentDto.class, Accomplishment.class);

        accomplDtoTypeMap.addMappings(
                (ConfigurableMapExpression<Accomplishment, AccomplishmentDto>
                         mapping) -> {
                    mapping.using(accomplConverters.getDtoNameConverter()).map(
                            name -> name, AccomplishmentDto::setMemberName);
                    mapping.using(accomplConverters.getDtoDateConverter()).map(
                            date -> date, AccomplishmentDto::setDate);
                });
        accomplTypeMap.addMappings(
                (ConfigurableMapExpression<AccomplishmentDto, Accomplishment>
                         mapping) -> {
                    mapping.using(accomplConverters.getAccProfConverter())
                            .map(profile -> profile,
                                    Accomplishment::setProfile);
                    mapping.using(accomplConverters.getAccDateConverter())
                            .map(date -> date,
                                    Accomplishment::setDate);
                });
    }

    public AccomplishmentDto mapFromAccomplishmentToDto(
            final Accomplishment accomplishment) {
        return accomplDtoTypeMap.map(accomplishment);
    }

    public Accomplishment mapFromDtoToAccomplishment(
            final AccomplishmentDto accomplishmentDto) {
        return accomplTypeMap.map(accomplishmentDto);
    }
}
