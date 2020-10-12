package com.ra.course.linkedin.persistence.comment;

import com.ra.course.linkedin.model.entity.other.Comment;
import com.ra.course.linkedin.persistence.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CommentRepositoryImpl extends BaseRepositoryImpl<Comment> implements CommentRepository {
    @Override
    public List<Comment> getCommentsByPostID(final Long postID) {
        return repository.stream().filter(comment -> comment.getPost().getId().equals(postID)).collect(Collectors.toList());
    }
}
