package com.ra.course.linkedin.service.post;

import com.ra.course.linkedin.model.entity.other.Post;
import com.ra.course.linkedin.persistence.post.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PostServiceImplMockTest {
    private static final long EXISTED_ID = 1L;
    private static final long NON_EXISTENT_ID = -1L;

    private final PostRepository mockPostRepository = mock(PostRepository.class);
    private final PostService postService =
            new PostServiceImpl(mockPostRepository);;

    private Post post;

    @BeforeEach
    public void before() {
        post = createPost();
    }

    @Test
    @DisplayName("When the post is added or updated, " +
            "then should be called PostRepository's addOrUpdatePost method.")
    public void testAddOrUpdatePost() {
        //when
        postService.addOrUpdatePost(post);
        //then
        verify(mockPostRepository).save(post);
    }

    @Test
    @DisplayName("When the post is deleted, " +
            "then should be called PostRepository's delete method.")
    public void testDeletePost() {
        //when
        postService.deletePost(post);
        //then
        verify(mockPostRepository).delete(post);
    }

    @Test
    @DisplayName("When get existed post, it should be returned.")
    public void testGetExistedPost() {
        //when
        when(mockPostRepository.getById(EXISTED_ID)).thenReturn(Optional.of(post));
        Optional<Post> actual = postService.getPostById(post.getId());
        //then
        assertEquals(actual, Optional.of(post));
        verify(mockPostRepository).getById(post.getId());
    }

    @Test
    @DisplayName("When get not existed post, " +
            "then must be returned empty optional.")
    public void testGetNotExistedPost() {
        //when
        Optional<Post> postOptional = postService.getPostById(NON_EXISTENT_ID);
        //then
        assertEquals(Optional.empty(), postOptional);
        verify(mockPostRepository).getById(NON_EXISTENT_ID);
    }

    @Test
    @DisplayName("When get all posts, then return list of all posts")
    void testGetAllPosts() {
        //when
        when(mockPostRepository.getAll()).thenReturn(List.of(post));
        List<Post> allPosts = postService.getAllPosts();
        //then
        assertEquals(allPosts, List.of(post));
        verify(mockPostRepository).getAll();
    }

    private Post createPost() {
        return Post.builder()
                .id(EXISTED_ID)
                .build();
    }
}
