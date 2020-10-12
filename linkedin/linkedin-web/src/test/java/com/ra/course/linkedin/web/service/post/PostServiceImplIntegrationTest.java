package com.ra.course.linkedin.web.service.post;

import com.ra.course.linkedin.model.entity.other.Post;
import com.ra.course.linkedin.persistence.post.PostRepository;
import com.ra.course.linkedin.service.post.PostService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ra.course.linkedin.web.service.ServiceIntegrationTestUtils.tryToDelete;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PostServiceImplIntegrationTest {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    private Post post;

    @BeforeEach
    public void before() {
        post = new Post();
        postService.addOrUpdatePost(post);
    }

    @AfterEach
    void tearDown() {
        tryToDelete(postRepository, post);
    }

    @Test
    @DisplayName("When post added or updated, then this post should " +
            "be present in repository")
    public void testAddPost() {
        //then
        assertTrue(getAllPostsFromRepository().contains(post));
    }

    @Test
    @DisplayName("When post was deleted, then this post should " +
            "be absent in repository")
    public void testDeletePost() {
        //when
        postService.deletePost(post);
        //then
        assertFalse(getAllPostsFromRepository().contains(post));
    }

    @Test
    @DisplayName("When get existed post, it should be returned.")
    public void testGetExistedPost() {
        //then
        assertEquals(postService.getPostById(post.getId()).get(), post);
    }

    @Test
    @DisplayName("When get not existed post, then throw EntityNotFoundException.")
    public void testGetNotExistedPost() {
        //when
        postService.deletePost(post);
        Optional<Post> postOptional = postService.getPostById(post.getId());
        //then
        assertTrue(postOptional.isEmpty());
    }

    @Test
    @DisplayName("When get all posts, then return List of all posts")
    void testGetAllPosts() {
        //then
        assertEquals(postService.getAllPosts(), postRepository.getAll());
    }

    private List<Post> getAllPostsFromRepository() {
        return new ArrayList<>(postService.getAllPosts());
    }
}
