package com.ra.course.linkedin.persistence.post;

import com.ra.course.linkedin.persistence.configuration.RepositoryConfiguration;
import com.ra.course.linkedin.persistence.utils.RepositoryTestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = RepositoryConfiguration.class)
public class PostRepositoryImplIntegrationTest {

    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("When repository of posts was created, then its size should be equal to 0")
    public void postRepositoryCreationTest() {
        assertEquals(0, RepositoryTestUtil.getSizeOfRepository(postRepository));
    }
}
