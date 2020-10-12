package com.ra.course.linkedin.persistence.stats;

import com.ra.course.linkedin.persistence.configuration.RepositoryConfiguration;
import com.ra.course.linkedin.persistence.utils.RepositoryTestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = RepositoryConfiguration.class)
public class StatsRepositoryImplIntegrationTest {

    @Autowired
    StatsRepository statsRepository;

    @Test
    @DisplayName("When repository of stats was created, then its size should be equal to 0")
    public void statsRepositoryCreationTest() {
        assertEquals(0, RepositoryTestUtil.getSizeOfRepository(statsRepository));
    }
}
