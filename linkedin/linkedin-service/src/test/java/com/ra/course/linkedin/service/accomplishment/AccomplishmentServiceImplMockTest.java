package com.ra.course.linkedin.service.accomplishment;

import com.ra.course.linkedin.model.entity.profile.Accomplishment;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.persistence.accomplishment.AccomplishmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AccomplishmentServiceImplMockTest {

    private static final long EXISTED_ID = 1L;
    private static final long NON_EXISTENT_ID = -1L;

    private final AccomplishmentRepository mockAccomplishmentRepository = mock(AccomplishmentRepository.class);
    private final AccomplishmentService accomplishmentService =
            new AccomplishmentServiceImpl(mockAccomplishmentRepository);

    private Accomplishment accomplishment;

    @BeforeEach
    public void setUp() {
        accomplishment = createAccomplishment();
    }

    @Test
    @DisplayName("When the accomplishment is added or updated, " +
            "then should be called Accomplishment's save method")
    public void testAddOrUpdateAccomplishment() {
        //when
        accomplishmentService.addOrUpdateAccomplishment(accomplishment);
        //then
        verify(mockAccomplishmentRepository).save(accomplishment);
    }

    @Test
    @DisplayName("When the accomplishment is deleted, then should be called " +
            "AccomplishmentRepository's delete method.")
    public void testDeleteAccomplishment() {
        //when
        accomplishmentService.deleteAccomplishment(accomplishment);
        //then
        verify(mockAccomplishmentRepository).delete(accomplishment);
    }

    @Test
    @DisplayName("When get existed accomplishment, it should be returned.")
    public void testGetExistedAccomplishment() {
        //when
        when(mockAccomplishmentRepository.getById(EXISTED_ID))
                .thenReturn(Optional.of(accomplishment));
        Optional<Accomplishment> result = accomplishmentService
                .getAccomplishmentById(accomplishment.getId());
        //then
        assertEquals(Optional.of(accomplishment), result);
        verify(mockAccomplishmentRepository).getById(accomplishment.getId());
    }

    @Test
    @DisplayName("When get not existed accomplishment, " +
            "then must be returned empty optional.")
    public void testGetNotExistedAccomplishment() {
        //when
        Optional<Accomplishment> accomplishmentOptional =
                accomplishmentService.getAccomplishmentById(NON_EXISTENT_ID);
        //then
        assertEquals(Optional.empty(), accomplishmentOptional);
        verify(mockAccomplishmentRepository).getById(NON_EXISTENT_ID);
    }


    @Test
    @DisplayName("When get accomplishments with specific profileId, " +
            "then return list of accomplishments with this profileId")
    void testGetAllAccomplishmentsByProfileID() {
        //given
        Accomplishment accomplishmentWithProfileWithExistingId =
                Accomplishment.builder()
                        .profile(Profile.builder()
                                .id(EXISTED_ID).build())
                        .build();
        //when
        when(accomplishmentService.getAccomplishmentsByProfileID(EXISTED_ID))
                .thenReturn(List.of(accomplishmentWithProfileWithExistingId));
        List<Accomplishment> accomplishmentList =
                accomplishmentService.getAccomplishmentsByProfileID(EXISTED_ID);
        //then
        assertEquals(List.of(accomplishmentWithProfileWithExistingId),
                accomplishmentList);
        verify(mockAccomplishmentRepository).getAccomplishmentsByProfileID(
                accomplishmentWithProfileWithExistingId.getProfile().getId());
    }

    private Accomplishment createAccomplishment() {
        return Accomplishment.builder()
                .id(EXISTED_ID)
                .build();
    }
}