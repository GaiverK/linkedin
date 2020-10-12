package com.ra.course.linkedin.web.mapper;

import com.ra.course.linkedin.model.entity.other.Comment;
import com.ra.course.linkedin.web.converter.CommentConverters;
import com.ra.course.linkedin.web.dto.comment.CommentDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.builder.ConfigurableMapExpression;
import org.springframework.stereotype.Component;

@Component
public class CommentMapperUtil {
    private final transient TypeMap<Comment, CommentDTO> commentDtoTypeMap;
    private final transient TypeMap<CommentDTO, Comment> commentTypeMap;

    public CommentMapperUtil(final ModelMapper modelMapper,
                             final CommentConverters commentConverters) {

        commentDtoTypeMap = modelMapper.createTypeMap(Comment.class, CommentDTO.class);
        commentTypeMap = modelMapper.createTypeMap(CommentDTO.class, Comment.class);

        commentTypeMap.addMappings((ConfigurableMapExpression<CommentDTO, Comment> mapper) -> {
            mapper.using(commentConverters.getDtoMembrConverter()).map(
                    member -> member, Comment::setAuthor);
            mapper.using(commentConverters.getDtoPostConverter()).map(
                    post -> post, Comment::setPost);
        });
    }

    public CommentDTO map(final Comment comment) {
        return commentDtoTypeMap.map(comment);
    }

    public Comment map(final CommentDTO commentDTO) {
        return commentTypeMap.map(commentDTO);
    }
}
