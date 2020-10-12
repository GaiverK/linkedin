package com.ra.course.linkedin.service.comment;

import com.ra.course.linkedin.model.entity.other.Comment;
import com.ra.course.linkedin.model.entity.other.Post;
import com.ra.course.linkedin.persistence.comment.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CommentServiceImplMockTest {
    public static final long COMMENT_ID = 1L;
    public static final long POST_ID = 1;
    public static final int EXPECTED_LIST_SIZE = 1;
    private CommentService commentService;
    private final CommentRepository mockCommentRepository = mock(CommentRepository.class);

    private Comment comment;

    @BeforeEach
    public void before() {
        commentService = new CommentServiceImpl(mockCommentRepository);
        comment = new Comment();
    }

    @Test
    @DisplayName("When comment exists then comment.text == comment")
    public void getCommentByID() {
        //when
        comment.setText("comment");
        when(commentService.getCommentByID(anyLong())).thenReturn(Optional.of(comment));
        commentService.getCommentByID(COMMENT_ID);
        //then
        assertEquals("comment", commentService.getCommentByID(COMMENT_ID).get().getText());
    }

    @Test
    @DisplayName("When comment not exists then return Optional.empty")
    public void getCommentByIDWhenNotExists() {
        //when
        when(commentService.getCommentByID(anyLong())).thenReturn(Optional.empty());
        //then
        assertEquals(commentService.getCommentByID(COMMENT_ID), Optional.empty());
    }

    @Test
    @DisplayName("When comments in post exists then return comments")
    public void getCommentsByPostID() {
        //given
        Post post = new Post();
        post.setId(POST_ID);
        comment.setPost(post);
        //when
        when(commentService.getCommentsByPostID(anyLong())).thenReturn(List.of(comment));
        //then
        assertEquals(commentService.getCommentsByPostID(POST_ID).size(), EXPECTED_LIST_SIZE);
    }

    @Test
    @DisplayName("When comments not exists then return empty list")
    public void getCommentsByPostIDWhenNotExists() {
        //when
        when(commentService.getCommentsByPostID(anyLong())).thenReturn(Collections.emptyList());
        //then
        assertEquals(commentService.getCommentsByPostID(POST_ID), Collections.emptyList());
    }

    @Test
    @DisplayName("When the comment is added or updated," +
            "then should be called CommentRepository's save method")
    public void testAddOrUpdateComment() {
        //when
        commentService.addOrUpdateComment(comment);
        //then
        verify(mockCommentRepository).save(comment);
    }

    @Test
    @DisplayName("When the comment is deleted, " +
            "then should be called CommentRepository's delete method")
    public void testDeleteComment() {
        //when
        commentService.deleteComment(comment);
        //then
        verify(mockCommentRepository).delete(comment);
    }
}
