package com.ra.course.linkedin.service.utils;

import com.ra.course.linkedin.model.entity.exceptions.EntityNotFoundException;
import com.ra.course.linkedin.model.entity.profile.*;
import com.ra.course.linkedin.persistence.accomplishment.AccomplishmentRepository;
import com.ra.course.linkedin.persistence.education.EducationRepository;
import com.ra.course.linkedin.persistence.experience.ExperienceRepository;
import com.ra.course.linkedin.persistence.profile.ProfileRepository;
import com.ra.course.linkedin.persistence.recommendation.RecommendationRepository;
import com.ra.course.linkedin.persistence.skill.SkillRepository;
import com.ra.course.linkedin.persistence.stats.StatsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProfileUtilsMockTest {

    private ProfileUtils profileUtils;

    private final ProfileRepository mockProfileRepository =
            mock(ProfileRepository.class);
    private final AccomplishmentRepository mockAccomplishmentRepository =
            mock(AccomplishmentRepository.class);
    private final EducationRepository mockEducationRepository =
            mock(EducationRepository.class);
    private final ExperienceRepository mockExperienceRepository =
            mock(ExperienceRepository.class);
    private final RecommendationRepository mockRecommendationRepository =
            mock(RecommendationRepository.class);
    private final SkillRepository mockSkillRepository =
            mock(SkillRepository.class);
    private final StatsRepository mockStatsRepository =
            mock(StatsRepository.class);

    @BeforeEach
    public void before() {
        profileUtils = new ProfileUtils(mockProfileRepository,
                mockAccomplishmentRepository,
                mockEducationRepository,
                mockExperienceRepository,
                mockRecommendationRepository,
                mockSkillRepository,
                mockStatsRepository);
    }

    @Test
    @DisplayName("When profile exists in repository, " +
            "then return this profile")

    public void testFindProfileWhenProfileExists() {
        //given
        Profile profile = createProfile();
        //when
        when(mockProfileRepository.getById(profile.getId()))
                .thenReturn(Optional.of(profile));
        Profile actual = profileUtils.findProfile(profile);
        //then
        assertEquals(actual, profile);
    }

    @Test
    @DisplayName("When profile does not exists in repository, " +
            "then throw EntityNotFoundException")

    public void testFindProfileWhenProfileDoesNotExist() {
        //given
        Profile profile = createProfile();
        //when
        when(mockProfileRepository.getById(profile.getId()))
                .thenReturn(Optional.empty());
        //then
        EntityNotFoundException thrown =
                assertThrows(EntityNotFoundException.class,
                        () -> profileUtils.findProfile(profile));
        assertTrue(thrown.getMessage().contains(Profile.class.getSimpleName()));
    }

    @Test
    @DisplayName("When accomplishment exists in repository, " +
            "then return this accomplishment")
    public void testFindAccomplishmentWhenAccomplishmentExists() {
        //given
        Accomplishment accomplishment = createAccomplishment();
        //when
        when(mockAccomplishmentRepository.getById(accomplishment.getId()))
                .thenReturn(Optional.of(accomplishment));
        Accomplishment actual = profileUtils.findAccomplishment(accomplishment);
        //then
        assertEquals(actual, accomplishment);
    }

    @Test
    @DisplayName("When accomplishment does not exist in repository, " +
            "then throw EntityNotFoundException")
    public void testFindAccomplishmentWhenAccomplishmentDoesNotExist() {
        //given
        Accomplishment accomplishment = createAccomplishment();
        //when
        when(mockAccomplishmentRepository.getById(accomplishment.getId()))
                .thenReturn(Optional.empty());
        //then
        EntityNotFoundException thrown =
                assertThrows(EntityNotFoundException.class,
                        () -> profileUtils.findAccomplishment(accomplishment));
        assertTrue(thrown.getMessage().contains(Accomplishment.class.getSimpleName()));
    }

    @Test
    @DisplayName("When education exists in repository, " +
            "then return this education")
    public void testFindEducationWhenEducationExists() {
        //given
        Education education = createEducation();
        //when
        when(mockEducationRepository.getById(education.getId()))
                .thenReturn(Optional.of(education));
        Education actual = profileUtils.findEducation(education);
        //then
        assertEquals(actual, education);
    }

    @Test
    @DisplayName("When education does not exist in repository, " +
            "then throw EntityNotFoundException")
    public void testFindEducationWhenEducationDoesNotExist() {
        //given
        Education education = createEducation();
        //when
        when(mockEducationRepository.getById(education.getId()))
                .thenReturn(Optional.empty());
        //then
        EntityNotFoundException thrown =
                assertThrows(EntityNotFoundException.class,
                        () -> profileUtils.findEducation(education));
        assertTrue(thrown.getMessage().contains(Education.class.getSimpleName()));
    }

    @Test
    @DisplayName("When experience exists in repository, " +
            "then return this experience")
    public void testFindExperienceWhenExperienceExists() {
        //given
        Experience experience = createExperience();
        //when
        when(mockExperienceRepository.getById(experience.getId()))
                .thenReturn(Optional.of(experience));
        Experience actual = profileUtils.findExperience(experience);
        //then
        assertEquals(actual, experience);
    }

    @Test
    @DisplayName("When experience does not exist in repository, " +
            "then throw EntityNotFoundException")
    public void testFindExperienceWhenExperienceDoesNotExist() {
        //given
        Experience experience = createExperience();
        //when
        when(mockExperienceRepository.getById(experience.getId()))
                .thenReturn(Optional.empty());
        //then
        EntityNotFoundException thrown =
                assertThrows(EntityNotFoundException.class,
                        () -> profileUtils.findExperience(experience));
        assertTrue(thrown.getMessage().contains(Experience.class.getSimpleName()));
    }

    @Test
    @DisplayName("When recommendation exists in repository, " +
            "then return this recommendation")
    public void testFindRecommendationWhenRecommendationExists() {
        //given
        Recommendation recommendation = createRecommendation();
        //when
        when(mockRecommendationRepository.getById(recommendation.getId()))
                .thenReturn(Optional.of(recommendation));
        Recommendation actual = profileUtils.findRecommendation(recommendation);
        //then
        assertEquals(actual, recommendation);
    }

    @Test
    @DisplayName("When recommendation does not exist in repository, " +
            "then throw EntityNotFoundException")
    public void testFindRecommendationWhenRecommendationDoesNotExist() {
        //given
        Recommendation recommendation = createRecommendation();
        //when
        when(mockRecommendationRepository.getById(recommendation.getId()))
                .thenReturn(Optional.empty());
        //then
        EntityNotFoundException thrown =
                assertThrows(EntityNotFoundException.class,
                        () -> profileUtils.findRecommendation(recommendation));
        assertTrue(thrown.getMessage().contains(Recommendation.class.getSimpleName()));
    }

    @Test
    @DisplayName("When skill exists in repository, " +
            "then return this skill")
    public void testFindSkillWhenSkillExists() {
        //given
        Skill skill = createSkill();
        //when
        when(mockSkillRepository.getById(skill.getId()))
                .thenReturn(Optional.of(skill));
        Skill actual = profileUtils.findSkill(skill);
        //then
        assertEquals(actual, skill);
    }

    @Test
    @DisplayName("When skill does not exist in repository, " +
            "then throw EntityNotFoundException")
    public void testFindSkillWhenSkillDoesNotExist() {
        //given
        Skill skill = createSkill();
        //when
        when(mockSkillRepository.getById(skill.getId()))
                .thenReturn(Optional.empty());
        //then
        EntityNotFoundException thrown =
                assertThrows(EntityNotFoundException.class,
                        () -> profileUtils.findSkill(skill));
        assertTrue(thrown.getMessage().contains(Skill.class.getSimpleName()));
    }

    @Test
    @DisplayName("When stats exists in repository, " +
            "then return this stats")
    public void testFindStatsWhenStatsExists() {
        //given
        Stats stats = createStats();
        //when
        when(mockStatsRepository.getById(stats.getId()))
                .thenReturn(Optional.of(stats));
        Stats actual = profileUtils.findStats(stats);
        //then
        assertEquals(actual, stats);
    }

    @Test
    @DisplayName("When stats does not exist in repository, " +
            "then throw EntityNotFoundException")
    public void testFindStatsWhenStatsDoesNotExist() {
        //given
        Stats stats = createStats();
        //when
        when(mockStatsRepository.getById(stats.getId()))
                .thenReturn(Optional.empty());
        //then
        EntityNotFoundException thrown =
                assertThrows(EntityNotFoundException.class,
                        () -> profileUtils.findStats(stats));
        assertTrue(thrown.getMessage().contains(Stats.class.getSimpleName()));
    }

    private Profile createProfile() {
        return Profile.builder()
                .id(1L)
                .build();
    }

    private Accomplishment createAccomplishment() {
        return Accomplishment.builder()
                .id(1L)
                .build();
    }

    private Education createEducation() {
        return Education.builder()
                .id(1L)
                .build();
    }


    private Experience createExperience() {
        return Experience.builder()
                .id(1L)
                .build();
    }

    private Recommendation createRecommendation() {
        return Recommendation.builder()
                .id(1L)
                .build();
    }

    private Skill createSkill() {
        return Skill.builder()
                .id(1L)
                .build();
    }

    private Stats createStats() {
        return Stats.builder()
                .id(1L)
                .build();
    }
}