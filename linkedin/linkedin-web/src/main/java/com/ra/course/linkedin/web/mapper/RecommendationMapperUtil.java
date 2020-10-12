package com.ra.course.linkedin.web.mapper;

import com.ra.course.linkedin.model.entity.profile.Recommendation;
import com.ra.course.linkedin.web.converter.RecommendationConverters;
import com.ra.course.linkedin.web.dto.recommendation.RecommendationDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.builder.ConfigurableMapExpression;
import org.springframework.stereotype.Component;

import static org.modelmapper.convention.MatchingStrategies.STANDARD;

@Component
public class RecommendationMapperUtil {

    private transient final TypeMap<Recommendation, RecommendationDto>
            recommDtoTypeMap;
    private transient final TypeMap<RecommendationDto, Recommendation>
            recommTypeMap;

    public RecommendationMapperUtil(final ModelMapper modelMapper,
                                    final RecommendationConverters recommConverters) {
        modelMapper.getConfiguration().setMatchingStrategy(STANDARD);
        recommDtoTypeMap = modelMapper.createTypeMap(
                Recommendation.class, RecommendationDto.class);
        recommTypeMap = modelMapper.createTypeMap(
                RecommendationDto.class, Recommendation.class);
        recommDtoTypeMap.addMappings(
                (ConfigurableMapExpression<Recommendation, RecommendationDto>
                         mapping) -> {
                    mapping.using(recommConverters.getDtoNameConverter()).map(
                            memberName -> memberName, RecommendationDto::setMemberName);
                    mapping.using(recommConverters.getDtoDateConverter()).map(
                            createdOn -> createdOn, RecommendationDto::setCreatedOn);
                    mapping.using(recommConverters.getDtoIdConverter()).map(
                            authorId -> authorId, RecommendationDto::setAuthorId);
                });
        recommTypeMap.addMappings(
                (ConfigurableMapExpression<RecommendationDto, Recommendation>
                         mapping) -> {
                    mapping.using(recommConverters.getRecProfConverter()).map(
                            profile -> profile, Recommendation::setProfile);
                    mapping.using(recommConverters.getRecDateConverter()).map(
                            createdOn -> createdOn, Recommendation::setCreatedOn);
                    mapping.using(recommConverters.getRecAuthConverter()).map(
                            author -> author, Recommendation::setAuthor);
                });
    }

    public RecommendationDto mapFromRecommendationToDto(
            final Recommendation recommendation) {
        return recommDtoTypeMap.map(recommendation);
    }

    public Recommendation mapFromDtoToRecommendation(
            final RecommendationDto recommendationDto) {
        return recommTypeMap.map(recommendationDto);
    }
}
