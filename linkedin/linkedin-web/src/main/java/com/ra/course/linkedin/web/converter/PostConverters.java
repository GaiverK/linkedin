package com.ra.course.linkedin.web.converter;

import com.ra.course.linkedin.model.entity.other.Post;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.service.comment.CommentService;
import com.ra.course.linkedin.service.member.MemberService;
import com.ra.course.linkedin.web.dto.post.PostDto;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;

@Component
public class PostConverters {

    private final Converter<Post, Long> dtoIdConverter;
    private final Converter<Post, Integer> dtoIntConverter;
    private final Converter<PostDto, Member> postAuthConverter;

    public PostConverters(final MemberService memberService,
                          final CommentService commentService) {
        dtoIdConverter = context ->
                context.getSource().getAuthor().getProfile().getId();

        dtoIntConverter = context ->
                commentService.getCommentsByPostID(context.getSource().getId()).size();

        postAuthConverter = context -> memberService.getMemberById(
                context.getSource().getAuthorId()).get();
    }

    public Converter<Post, Long> getDtoIdConverter() {
        return dtoIdConverter;
    }

    public Converter<Post, Integer> getDtoIntConverter() {
        return dtoIntConverter;
    }

    public Converter<PostDto, Member> getPostAuthConverter() {
        return postAuthConverter;
    }
}
