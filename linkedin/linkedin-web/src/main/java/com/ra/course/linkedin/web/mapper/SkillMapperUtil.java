package com.ra.course.linkedin.web.mapper;

import com.ra.course.linkedin.model.entity.profile.Skill;
import com.ra.course.linkedin.web.converter.SkillConverters;
import com.ra.course.linkedin.web.dto.skill.SkillDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.builder.ConfigurableMapExpression;
import org.springframework.stereotype.Component;

import static org.modelmapper.convention.MatchingStrategies.STANDARD;

@Component
public class SkillMapperUtil {

    private transient final TypeMap<Skill, SkillDto> skillDtoTypeMap;
    private transient final TypeMap<SkillDto, Skill> skillTypeMap;

    public SkillMapperUtil(final ModelMapper modelMapper,
                           final SkillConverters skillConverters) {
        modelMapper.getConfiguration().setMatchingStrategy(STANDARD);

        this.skillDtoTypeMap = modelMapper.createTypeMap(Skill.class, SkillDto.class);
        this.skillTypeMap = modelMapper.createTypeMap(SkillDto.class, Skill.class);

        skillDtoTypeMap.addMappings(
                (ConfigurableMapExpression<Skill, SkillDto> mapping) -> {
                    mapping.using(skillConverters.getDtoNameConverter()).map(
                            name -> name, SkillDto::setMemberName);
                });
        skillTypeMap.addMappings(
                (ConfigurableMapExpression<SkillDto, Skill> mapping) -> {
                    mapping.using(skillConverters.getSkilProfConverter()).map(
                            profile -> profile, Skill::setProfile);
                });
    }

    public SkillDto mapFromSkillToDto(final Skill skill) {
        return skillDtoTypeMap.map(skill);
    }

    public Skill mapFromDtoToSkill(final SkillDto skillDto) {
        return skillTypeMap.map(skillDto);
    }
}
