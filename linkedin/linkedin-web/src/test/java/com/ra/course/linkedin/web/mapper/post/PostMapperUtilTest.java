package com.ra.course.linkedin.web.mapper.post;

import com.ra.course.linkedin.model.entity.other.Comment;
import com.ra.course.linkedin.model.entity.other.Post;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.service.comment.CommentService;
import com.ra.course.linkedin.service.member.MemberService;
import com.ra.course.linkedin.web.dto.post.PostDto;
import com.ra.course.linkedin.web.mapper.PostMapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static com.ra.course.linkedin.web.controller.profile.ProfileControllerMockMvcTest.SOME_ID_22;
import static com.ra.course.linkedin.web.controller.recommendation.RecommendationControllerMockMvcTest.SOME_ID_33;
import static com.ra.course.linkedin.web.controller.recommendation.RecommendationControllerMockMvcTest.SOME_ID_44;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostMapperUtilTest {

    @MockBean
    private MemberService memberService;
    @MockBean
    private CommentService commentService;
    @Autowired
    private PostMapperUtil postMapper;

    private Post post;
    private PostDto postDto;

    @BeforeEach
    void setUp() {
        post = createPost();
        postDto = createPostDto();
    }

    @Test
    @DisplayName("Expect postDto is mapped properly")
    public void testMapFromPostToDtoWhenCommentListSizeIsNotZero() {
        //when
        when(commentService.getCommentsByPostID(anyLong()))
                .thenReturn(post.getComments());
        PostDto result = postMapper.mapFromPostToDto(post);
        //then
        assertEquals(postDto, result);
    }

    @Test
    @DisplayName("Expect postDto is mapped properly")
    public void testMapFromPostToDtoWhenCommentListSizeIsZero() {
        //given
        post.setComments(new ArrayList<>());
        postDto.setCommentsSize(0);
        //when
        PostDto result = postMapper.mapFromPostToDto(post);
        //then
        assertEquals(postDto, result);
    }

    @Test
    @DisplayName("Expect post has correct parameters ")
    public void testMapFromDtoToPost() {
        //when
        when(memberService.getMemberById(postDto.getAuthorId()))
                .thenReturn(ofNullable(post.getAuthor()));
        Post result = postMapper.mapFromDtoToPost(postDto);
        //then
        assertAll(
                () -> assertEquals(post.getAuthor(), result.getAuthor()),
                () -> assertEquals(post.getId(), result.getId()),
                () -> assertEquals(post.getText(), result.getText()));
    }

    private Post createPost() {
        Member author = Member.builder()
                .profile(Profile.builder().id(SOME_ID_33)
                        .build())
                .id(SOME_ID_33)
                .name("authorName")
                .build();

        List<Comment> comments = new ArrayList<>();
        Comment comment = Comment.builder()
                .id(SOME_ID_44)
                .build();
        comments.add(comment);

        return Post.builder()
                .id(SOME_ID_22)
                .author(author)
                .text("Text")
                .comments(comments)
                .build();
    }

    private PostDto createPostDto() {
        postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setProfileId(post.getAuthor().getProfile().getId());
        postDto.setAuthorId(post.getAuthor().getId());
        postDto.setAuthorName(post.getAuthor().getName());
        postDto.setText(post.getText());
        postDto.setCommentsSize(post.getComments().size());

        return postDto;
    }

}
