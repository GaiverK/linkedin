package com.ra.course.linkedin.web.converter;

import com.ra.course.linkedin.model.entity.other.Post;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.service.member.MemberService;
import com.ra.course.linkedin.service.post.PostService;
import com.ra.course.linkedin.web.dto.comment.CommentDTO;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommentConverters {

    private final Converter<CommentDTO, Member> dtoMembrConverter;
    private final Converter<CommentDTO, Post> dtoPostConverter;

    public CommentConverters(final MemberService memberService,
                             final PostService postService) {
        dtoMembrConverter = context -> memberService
                .getMemberById(context.getSource().getAuthorID()).get();
        dtoPostConverter = context -> postService
                .getPostById(context.getSource().getPostID()).get();
    }

    public Converter<CommentDTO, Member> getDtoMembrConverter() {
        return dtoMembrConverter;
    }

    public Converter<CommentDTO, Post> getDtoPostConverter() {
        return dtoPostConverter;
    }
}
