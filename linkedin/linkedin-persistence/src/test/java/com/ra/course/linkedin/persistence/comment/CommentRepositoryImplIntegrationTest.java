package com.ra.course.linkedin.persistence.comment;

import com.ra.course.linkedin.model.entity.other.Comment;
import com.ra.course.linkedin.model.entity.other.Post;
import com.ra.course.linkedin.persistence.configuration.RepositoryConfiguration;
import com.ra.course.linkedin.persistence.post.PostRepository;
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
public class CommentRepositoryImplIntegrationTest {

    private static final long NON_EXISTENT_ID = -1L;

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;

    private Post post;

    private List<Comment> searchedComments;

    @BeforeEach
    public void setUp() {
        post = new Post();
        postRepository.save(post);
        searchedComments = createSearchedComments();
        searchedComments.forEach(commentRepository::save);
    }

    @AfterEach
    public void tearDown() {
        postRepository.delete(post);
        searchedComments.forEach(commentRepository::delete);
    }

    @Test
    @DisplayName("When searched comments exist, then must be returned their list")
    void testWhenSearchedCommentsExist() {
        //when
        List<Comment> commentsByPostId =
                commentRepository.getCommentsByPostID(post.getId());
        //then
        assertAll(
                () -> assertEquals(commentsByPostId.size(), 3),
                () -> assertEquals(commentsByPostId.get(0).getText(),
                        createSearchedComments().get(0).getText()),
                () -> assertEquals(commentsByPostId.get(1).getText(),
                        createSearchedComments().get(1).getText()),
                () -> assertEquals(commentsByPostId.get(2).getText(),
                        createSearchedComments().get(2).getText())
        );
    }

    @Test
    @DisplayName("When searched comments do not exist, " +
            "then must be returned empty list")
    void testWhenSearchedCommentsDoNotExist() {
        //when
        List<Comment> commentsByPostId =
                commentRepository.getCommentsByPostID(NON_EXISTENT_ID);
        //then
        assertEquals(commentsByPostId.size(), 0);
    }

    private List<Comment> createSearchedComments() {
        List<Comment> searchedComments = new ArrayList<>();
        Comment comment = Comment.builder()
                .post(post)
                .text("comment")
                .build();
        Comment comment1 = Comment.builder()
                .post(post)
                .text("comment1")
                .build();
        Comment comment2 = Comment.builder()
                .post(post)
                .text("comment2")
                .build();
        searchedComments.add(comment);
        searchedComments.add(comment1);
        searchedComments.add(comment2);

        return searchedComments;
    }
}
