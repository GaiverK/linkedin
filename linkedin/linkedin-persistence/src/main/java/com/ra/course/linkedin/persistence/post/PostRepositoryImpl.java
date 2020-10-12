package com.ra.course.linkedin.persistence.post;

import com.ra.course.linkedin.model.entity.other.Post;
import com.ra.course.linkedin.persistence.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository
public class PostRepositoryImpl extends BaseRepositoryImpl<Post> implements PostRepository {
}
