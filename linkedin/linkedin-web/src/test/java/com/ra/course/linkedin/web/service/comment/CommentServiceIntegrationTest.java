package com.ra.course.linkedin.web.service.comment;

import com.ra.course.linkedin.model.entity.other.Comment;
import com.ra.course.linkedin.persistence.comment.CommentRepository;
import com.ra.course.linkedin.service.comment.CommentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.ra.course.linkedin.web.service.ServiceIntegrationTestUtils.tryToDelete;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CommentServiceIntegrationTest {

    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;

    private Comment comment;

    @BeforeEach
    public void before() {
        comment = new Comment();
    }

    @AfterEach
    void tearDown() {
        tryToDelete(commentRepository, comment);
    }

    @Test
    @DisplayName("When comment exists then return comment")
    public void getCommentByID() {
        //given
        commentRepository.save(comment);
        //then
        assertEquals(comment, commentService.getCommentByID(comment.getId()).get());
    }

    @Test
    @DisplayName("When comment not exists then return Optional.empty")
    public void getCommentByIDWhenNotExists() {
        //given
        commentRepository.save(comment);
        commentRepository.delete(comment);
        //then
        assertEquals(commentService.getCommentByID(comment.getId()), Optional.empty());
    }

    @Test
    @DisplayName("When comment added or updated, then this comment should be present in repository")
    public void testAddOrUpdateComment() {
        //when
        commentService.addOrUpdateComment(comment);
        //then
        assertTrue(getAllCommentsFromRepository().contains(comment));
    }

    @Test
    @DisplayName("When comment deleted, then thos comment shouldn't be present in repository")
    public void testDeleteComment() {
        //given
        commentRepository.save(comment);
        //when
        commentService.deleteComment(comment);
        //then
        assertFalse(getAllCommentsFromRepository().contains(comment));
    }

    private List<Comment> getAllCommentsFromRepository() {
        return StreamSupport.stream(commentRepository.getAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}
