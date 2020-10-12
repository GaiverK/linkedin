package com.ra.course.linkedin.service.comment;

import com.ra.course.linkedin.model.entity.other.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<Comment> getCommentByID(Long commentID);

    List<Comment> getCommentsByPostID(Long postID);

    Comment addOrUpdateComment(Comment comment);

    void deleteComment(Comment comment);
}
