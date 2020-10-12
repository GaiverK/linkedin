package com.ra.course.linkedin.web.controller;

import com.ra.course.linkedin.model.entity.other.Post;
import com.ra.course.linkedin.service.comment.CommentService;
import com.ra.course.linkedin.service.post.PostService;
import com.ra.course.linkedin.web.dto.comment.CommentDTO;
import com.ra.course.linkedin.web.dto.post.PostDto;
import com.ra.course.linkedin.web.mapper.CommentMapperUtil;
import com.ra.course.linkedin.web.mapper.PostMapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {
    public static final String ADD_NEW_POST = "/addNewPost";
    public static final String POST_FORM = "postForm";
    public static final String REDIRECT_TO_POSTS =
            "redirect:/posts/getAllPosts";
    public static final String UPDATE_ID = "/update/{id}";
    public static final String POST = "post";
    public static final String POST_LIST = "postList";
    public static final String COMMENTS = "comments";
    public static final String POST_SINGLE_POST = "post/singlePost";

    private final PostService postService;
    private final CommentService commentService;
    private final PostMapperUtil postMapperUtil;
    private final CommentMapperUtil commentMapperUtil;

    @RequestMapping(value = ADD_NEW_POST, method = RequestMethod.GET)
    public String addPost(final Model model) {

        final PostDto postDto = new PostDto();
        model.addAttribute(POST_FORM, postDto);

        return "post/addPost";
    }

    @RequestMapping(value = ADD_NEW_POST, method = RequestMethod.POST)
    public String addPost(@ModelAttribute(POST_FORM) final PostDto postDto) {

        final Post post = postMapperUtil.mapFromDtoToPost(postDto);
        postService.addOrUpdatePost(post);

        return REDIRECT_TO_POSTS;
    }

    @RequestMapping(value = "/singlePost/{postId}/update", method = RequestMethod.GET)
    public String getPost(@PathVariable final long postId, final Model model) {

        final Post post = postService.getPostById(postId).get();
        final PostDto postDto = postMapperUtil.mapFromPostToDto(post);

        final List<CommentDTO> commentsDTO = commentService
                .getCommentsByPostID(post.getId())
                .stream()
                .map(commentMapperUtil::map)
                .collect(Collectors.toList());
        model.addAttribute(POST, postDto);
        model.addAttribute(COMMENTS, commentsDTO);
        model.addAttribute("operation", "update");

        return POST_SINGLE_POST;
    }

    @RequestMapping(value = UPDATE_ID, method = RequestMethod.POST)
    public String updatePost(final PostDto postDto) {

        final Post post = postMapperUtil.mapFromDtoToPost(postDto);
        postService.addOrUpdatePost(post);

        return REDIRECT_TO_POSTS;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deletePost(@PathVariable final long id) {

        postService.deletePost(postService.getPostById(id).get());

        return REDIRECT_TO_POSTS;
    }

    @GetMapping("/getAllPosts")
    public String getAllPosts(final Model model) {

        final List<PostDto> postDtoList = postService.getAllPosts()
                .stream()
                .map(postMapperUtil::mapFromPostToDto)
                .collect(Collectors.toList());
        Collections.reverse(postDtoList);
        model.addAttribute(POST_LIST, postDtoList);

        return "post/postsList";
    }

    @GetMapping("/singlePost/{postID}")
    public String viewAllPosts(@PathVariable final Long postID,
                               final Model model) {
        final Post post = postService.getPostById(postID).get();
        final PostDto postCommentDTO = postMapperUtil.mapFromPostToDto(post);
        final List<CommentDTO> commentsDTO = commentService
                .getCommentsByPostID(post.getId())
                .stream()
                .map(commentMapperUtil::map)
                .collect(Collectors.toList());
        model.addAttribute(POST, postCommentDTO);
        model.addAttribute(COMMENTS, commentsDTO);

        return POST_SINGLE_POST;
    }
}
