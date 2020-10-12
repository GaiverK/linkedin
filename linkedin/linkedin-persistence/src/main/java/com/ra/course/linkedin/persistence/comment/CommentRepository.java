package com.ra.course.linkedin.persistence.comment;

import com.ra.course.linkedin.model.entity.other.Comment;
import com.ra.course.linkedin.persistence.BaseRepository;

import java.util.List;

public interface CommentRepository extends BaseRepository<Comment> {
    List<Comment> getCommentsByPostID(Long postID);
}
