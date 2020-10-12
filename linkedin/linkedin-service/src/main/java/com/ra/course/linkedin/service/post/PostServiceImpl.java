package com.ra.course.linkedin.service.post;

import com.ra.course.linkedin.model.entity.other.Post;
import com.ra.course.linkedin.persistence.post.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public Post addOrUpdatePost(final Post post) {
        return postRepository.save(post);
    }

    @Override
    public void deletePost(final Post post) {
        postRepository.delete(post);
    }

    @Override
    public Optional<Post> getPostById(final long id) {
        return postRepository.getById(id);
    }

    @Override
    public List<Post> getAllPosts() {
        return StreamSupport.stream(postRepository.getAll().spliterator(),
                false)
                .collect(Collectors.toList());
    }
}
