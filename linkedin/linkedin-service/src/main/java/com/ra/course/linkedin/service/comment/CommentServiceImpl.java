package com.ra.course.linkedin.service.comment;

import com.ra.course.linkedin.model.entity.other.Comment;
import com.ra.course.linkedin.persistence.comment.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Optional<Comment> getCommentByID(final Long commentID) {
        return commentRepository.getById(commentID);
    }

    @Override
    public List<Comment> getCommentsByPostID(final Long postID) {
        return commentRepository.getCommentsByPostID(postID);
    }

    @Override
    public Comment addOrUpdateComment(final Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(final Comment comment) {
        commentRepository.delete(comment);
    }
}
