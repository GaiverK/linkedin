package com.ra.course.linkedin.persistence;

import com.ra.course.linkedin.model.entity.BaseEntity;
import com.ra.course.linkedin.model.entity.exceptions.EntityNotFoundException;
import com.ra.course.linkedin.persistence.configuration.RepositoryConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = RepositoryConfiguration.class)
class BaseRepositoryImplIntegrationTest {
    private final static Logger LOGGER =
            Logger.getLogger(BaseRepositoryImplIntegrationTest.class.getName());
    private static final String NEW_NAME = "NEW_NAME";
    public static final Long ID_NULL = null;

    @Autowired
    BaseRepository<BaseEntity> baseRepository;

    private TestBaseEntity baseEntity;

    @BeforeEach
    void setUp() {
        baseEntity = createTestBaseEntity();
        baseRepository.save(baseEntity);
    }

    @AfterEach
    void tearDown() {
        try {
            baseRepository.delete(baseEntity);
        } catch (Exception e) {
            LOGGER.info(String.format("%s. But here it is ok.",
                    e.getClass().getSimpleName()));
        }
    }

    @Test
    @DisplayName("When get entity by Id and it exists in repository, " +
            "then return Optional of this entity")
    void testGetEntityByIdWhenItExistsInRepo() {
        //when
        Optional<BaseEntity> entity = baseRepository.getById(baseEntity.getId());
        //then
        assertEquals(entity, Optional.of(baseEntity));
    }

    @Test
    @DisplayName("When get entity by Id and it does not exist in repository, " +
            "then must be returned empty optional")
    void testGetEntityByIdWhenItDoesNotExistsInRepo() {
        //when
        baseRepository.delete(baseEntity);
        Optional<BaseEntity> result = baseRepository.getById(baseEntity.getId());
        //then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("When get entity by Id and it does not exist in repository, " +
            "then must be returned empty optional")
    void testGetEntityByIdWhenItDoesNotExistsInRepoAndIdIsNull() {
        //when
        baseRepository.delete(baseEntity);
        baseEntity.setId(ID_NULL);
        Optional<BaseEntity> result = baseRepository.getById(baseEntity.getId());
        //then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("When the entity is saved and it does not exist in repository, " +
            "then it must be added to the repository")
    void testSaveEntityWhenItDoesNotExistInRepo() {
        //when
        baseRepository.delete(baseEntity);
        TestBaseEntity result = (TestBaseEntity) baseRepository.save(baseEntity);
        //then
        assertAll(
                () -> assertEquals(1,
                        ((List<BaseEntity>) baseRepository.getAll()).size()),
                () -> assertEquals(result, baseEntity)
        );
    }

    @Test
    @DisplayName("When the entity is saved and it exists in repository, " +
            "then it must be updated in the repository")
    void testSaveEntityWhenItExistsInRepo() {
        //given
        TestBaseEntity changedEntity = (TestBaseEntity) baseRepository.save(baseEntity);
        changedEntity.setName(NEW_NAME);
        //when
        TestBaseEntity result = (TestBaseEntity) baseRepository.save(changedEntity);
        //then
        assertAll(
                () -> assertEquals(1,
                        ((List<BaseEntity>) baseRepository.getAll()).size()),
                () -> assertEquals(result.getName(), NEW_NAME)
        );
    }

    @Test
    @DisplayName("When delete the entity, then it should not be in repository")
    void testDeleteEntityWhenItExistsInRepo() {
        //when
        baseRepository.delete(baseEntity);
        //then
        assertAll(
                () -> assertFalse(((List<BaseEntity>) baseRepository.getAll())
                        .contains(baseEntity)),
                () -> assertEquals(((List<BaseEntity>) baseRepository.getAll())
                        .size(), 0)
        );
    }

    @Test
    @DisplayName("When delete the entity which does not exist in repository, " +
            "then should be throw EntityNotFoundException with specific message")
    void testDeleteEntityWhenItDoesNotExistInRepo() {
        //given
        baseRepository.delete(baseEntity);
        //when
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> baseRepository.delete(baseEntity));
        //then
        assertTrue(exception.getMessage().contains(BaseEntity.class.getSimpleName()));
    }

    @Test
    @DisplayName("When get all entities, then return Iterable of all entities")
    void testGetAllEntities() {
        //then
        assertEquals(baseRepository.getAll(), List.of(baseEntity));
    }

    private class TestBaseEntity extends BaseEntity {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private TestBaseEntity createTestBaseEntity() {
        return new TestBaseEntity();
    }
}