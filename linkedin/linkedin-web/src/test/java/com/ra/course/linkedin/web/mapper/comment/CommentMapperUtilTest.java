package com.ra.course.linkedin.web.mapper.comment;

import com.ra.course.linkedin.model.entity.other.Comment;
import com.ra.course.linkedin.model.entity.other.Post;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.service.member.MemberService;
import com.ra.course.linkedin.service.post.PostService;
import com.ra.course.linkedin.web.dto.comment.CommentDTO;
import com.ra.course.linkedin.web.mapper.CommentMapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.ra.course.linkedin.web.controller.post.PostControllerMockMvcTest.SOME_ID_55;
import static com.ra.course.linkedin.web.controller.profile.ProfileControllerMockMvcTest.SOME_ID_22;
import static com.ra.course.linkedin.web.controller.recommendation.RecommendationControllerMockMvcTest.SOME_ID_33;
import static com.ra.course.linkedin.web.controller.recommendation.RecommendationControllerMockMvcTest.SOME_ID_44;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CommentMapperUtilTest {

    @MockBean
    private PostService postService;
    @MockBean
    private MemberService memberService;
    @Autowired
    private CommentMapperUtil commentMapper;

    private Comment comment;
    private CommentDTO commentDTO;

    @BeforeEach
    void setUp() {
        comment = createComment();
        commentDTO = createCommentDto();
    }

    @Test
    @DisplayName("Expect commentDTO is mapped properly")
    public void testMapFromCommentToDto() {
        //when
        CommentDTO result = commentMapper.map(comment);
        //then
        assertEquals(commentDTO, result);
    }

    @Test
    @DisplayName("Expect comment has correct parameters ")
    public void testMapFromDtoToComment() {
        //when
        when(memberService.getMemberById(commentDTO.getAuthorID()))
                .thenReturn(ofNullable(comment.getAuthor()));
        when(postService.getPostById(commentDTO.getPostID()))
                .thenReturn(ofNullable(comment.getPost()));
        Comment result = commentMapper.map(commentDTO);
        //then
        assertAll(
                () -> assertEquals(comment.getId(), result.getId()),
                () -> assertEquals(comment.getAuthor(), result.getAuthor()),
                () -> assertEquals(comment.getPost(), result.getPost()),
                () -> assertEquals(comment.getText(), result.getText()));
    }

    private Comment createComment() {
        Member author = Member.builder()
                .profile(Profile.builder().id(SOME_ID_44).build())
                .id(SOME_ID_33)
                .name("authorName")
                .build();
        Member postAuthor = Member.builder()
                .profile(Profile.builder().id(SOME_ID_33).build())
                .id(SOME_ID_44)
                .name("authorName")
                .build();

        Post post = Post.builder()
                .id(SOME_ID_55)
                .author(postAuthor)
                .build();

        return Comment.builder()
                .id(SOME_ID_22)
                .author(author)
                .post(post)
                .text("Text")
                .build();
    }

    private CommentDTO createCommentDto() {
        commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setAuthorID(comment.getAuthor().getId());
        commentDTO.setAuthorProfileId(comment.getAuthor().getProfile().getId());
        commentDTO.setAuthorName(comment.getAuthor().getName());
        commentDTO.setPostID(comment.getPost().getId());
        commentDTO.setText(comment.getText());

        return commentDTO;
    }

}
