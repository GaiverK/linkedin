package com.ra.course.linkedin.persistence.accomplishment;

import com.ra.course.linkedin.model.entity.profile.Accomplishment;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.persistence.configuration.RepositoryConfiguration;
import com.ra.course.linkedin.persistence.profile.ProfileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = RepositoryConfiguration.class)
public class AccomplishmentRepositoryImplIntegrationTest {

    private static final long NON_EXISTENT_ID = -1L;

    @Autowired
    AccomplishmentRepository accomplishmentRepository;
    @Autowired
    ProfileRepository profileRepository;

    private Profile profile;

    private List<Accomplishment> searchedAccomplishments;

    @BeforeEach
    public void setUp() {
        profile = new Profile();
        profileRepository.save(profile);
        searchedAccomplishments = createSearchedAccomplishments();
        searchedAccomplishments.forEach(accomplishmentRepository::save);
    }

    @AfterEach
    public void tearDown() {
        profileRepository.delete(profile);
        searchedAccomplishments.forEach(accomplishmentRepository::delete);
    }

    @Test
    @DisplayName("When searched accomplishments exist, " +
            "then must be returned their list")
    void testWhenSearchedAccomplishmentsExist() {
        //when
        List<Accomplishment> accomplishmentsByProfileId =
                accomplishmentRepository.getAccomplishmentsByProfileID(
                        profile.getId());
        //then
        assertAll(
                () -> assertEquals(accomplishmentsByProfileId.size(), 3),
                () -> assertEquals(accomplishmentsByProfileId.get(0).getTitle(),
                        createSearchedAccomplishments().get(0).getTitle()),
                () -> assertEquals(accomplishmentsByProfileId.get(1).getTitle(),
                        createSearchedAccomplishments().get(1).getTitle()),
                () -> assertEquals(accomplishmentsByProfileId.get(2).getTitle(),
                        createSearchedAccomplishments().get(2).getTitle())
        );
    }

    @Test
    @DisplayName("When searched accomplishments do not exist, " +
            "then must be returned empty list")
    void testWhenSearchedAccomplishmentsDoNotExist() {
        //when
        List<Accomplishment> accomplishmentsByProfileId =
                accomplishmentRepository.getAccomplishmentsByProfileID(
                        NON_EXISTENT_ID);
        //then
        assertEquals(accomplishmentsByProfileId.size(), 0);
    }

    private List<Accomplishment> createSearchedAccomplishments() {
        List<Accomplishment> searchedAccomplishments = new ArrayList<>();

        Accomplishment accomplishment = Accomplishment.builder()
                .profile(Profile.builder().id(profile.getId()).build())
                .title("accomplishment")
                .build();
        Accomplishment accomplishment1 = Accomplishment.builder()
                .title("accomplishment1")
                .profile(Profile.builder().id(profile.getId()).build())
                .build();
        Accomplishment accomplishment2 = Accomplishment.builder()
                .profile(Profile.builder().id(profile.getId()).build())
                .title("accomplishment2")
                .build();
        searchedAccomplishments.add(accomplishment);
        searchedAccomplishments.add(accomplishment1);
        searchedAccomplishments.add(accomplishment2);

        return searchedAccomplishments;
    }
}
