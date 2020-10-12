package com.ra.course.linkedin.web.controller;

import com.ra.course.linkedin.model.entity.other.Comment;
import com.ra.course.linkedin.service.comment.CommentService;
import com.ra.course.linkedin.web.dto.comment.CommentDTO;
import com.ra.course.linkedin.web.mapper.CommentMapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/comments")
@Controller
@AllArgsConstructor
public class CommentController {
    public static final String REDIR_SINGLE_POST = "redirect:/posts/singlePost/";
    private final transient CommentService commentService;
    private final transient CommentMapperUtil commentMapperUtil;

    @PostMapping("/add")
    public String addComment(@ModelAttribute final CommentDTO commentdto) {
        final Comment comment = commentMapperUtil.map(commentdto);
        commentService.addOrUpdateComment(comment);
        return REDIR_SINGLE_POST + comment.getPost().getId();
    }

    @GetMapping("/delete/{commentID}")
    public String deleteComment(@PathVariable final Long commentID) {
        final Comment comment = commentService.getCommentByID(commentID).get();
        commentService.deleteComment(comment);
        return REDIR_SINGLE_POST + comment.getPost().getId();
    }
}
