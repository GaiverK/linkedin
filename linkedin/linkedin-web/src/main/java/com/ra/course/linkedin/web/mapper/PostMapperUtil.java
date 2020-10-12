package com.ra.course.linkedin.web.mapper;

import com.ra.course.linkedin.model.entity.other.Post;
import com.ra.course.linkedin.web.converter.PostConverters;
import com.ra.course.linkedin.web.dto.post.PostDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.builder.ConfigurableMapExpression;
import org.springframework.stereotype.Component;

import static org.modelmapper.convention.MatchingStrategies.STANDARD;

@Component
public class PostMapperUtil {

    private final transient TypeMap<Post, PostDto> postDtoTypeMap;
    private final transient TypeMap<PostDto, Post> postTypeMap;

    public PostMapperUtil(final ModelMapper modelMapper,
                          final PostConverters postConverters) {
        modelMapper.getConfiguration().setMatchingStrategy(STANDARD);

        postDtoTypeMap = modelMapper.createTypeMap(Post.class, PostDto.class);
        postTypeMap = modelMapper.createTypeMap(PostDto.class, Post.class);

        postDtoTypeMap.addMappings((ConfigurableMapExpression<Post, PostDto>
                                            mapping) -> {
            mapping.using(postConverters.getDtoIdConverter()).map(
                    authorId -> authorId, PostDto::setProfileId);
            mapping.using(postConverters.getDtoIntConverter()).map(
                    commentListSize -> commentListSize,
                    PostDto::setCommentsSize);
        });

        postTypeMap.addMappings((ConfigurableMapExpression<PostDto, Post>
                                         mapping) -> {
            mapping.using(postConverters.getPostAuthConverter()).map(
                    author -> author, Post::setAuthor);
        });
    }

    public PostDto mapFromPostToDto(final Post post) {
        return postDtoTypeMap.map(post);
    }

    public Post mapFromDtoToPost(final PostDto postDto) {
        return postTypeMap.map(postDto);
    }
}