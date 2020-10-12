package com.ra.course.linkedin.service.post;

import com.ra.course.linkedin.model.entity.other.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Post addOrUpdatePost(Post post);

    void deletePost(Post post);

    Optional<Post> getPostById(long id);

    List<Post> getAllPosts();
}
