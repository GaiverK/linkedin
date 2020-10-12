package com.ra.course.linkedin.persistence.group;

import com.ra.course.linkedin.persistence.configuration.RepositoryConfiguration;
import com.ra.course.linkedin.persistence.utils.RepositoryTestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = RepositoryConfiguration.class)
public class GroupRepositoryImplIntegrationTest {

    @Autowired
    GroupRepository groupRepository;

    @Test
    @DisplayName("When repository of groups was created, then its size should be equal to 0")
    public void groupRepositoryCreationTest() {
        assertEquals(0, RepositoryTestUtil.getSizeOfRepository(groupRepository));
    }
}
