package com.ra.course.linkedin.web.controller.comment;

import com.ra.course.linkedin.model.entity.other.Comment;
import com.ra.course.linkedin.model.entity.other.Post;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.service.comment.CommentService;
import com.ra.course.linkedin.web.dto.comment.CommentDTO;
import com.ra.course.linkedin.web.mapper.CommentMapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.ra.course.linkedin.web.controller.profile.ProfileControllerMockMvcTest.SOME_ID_22;
import static com.ra.course.linkedin.web.controller.recommendation.RecommendationControllerMockMvcTest.SOME_ID_33;
import static com.ra.course.linkedin.web.controller.recommendation.RecommendationControllerMockMvcTest.SOME_ID_44;
import static java.util.Optional.ofNullable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerMockMvcTest {

    @MockBean
    private CommentService commentService;
    @MockBean
    private CommentMapperUtil commentMapper;

    @Autowired
    MockMvc mockMvc;

    private Comment comment;
    private CommentDTO commentDTO;

    @BeforeEach
    void setUp() {
        comment = createComment();
        commentDTO = createCommentDto();
    }

    @Test
    @DisplayName("Expect status is Redirection, redirected url is " +
            "/posts/singlePost/commentDTO.getPostID() and commentService " +
            "used method addOrUpdateComment with parameter comment")
    public void testAddComment() throws Exception {
        //when
        when(commentMapper.map((CommentDTO) any())).thenReturn(comment);
        //then
        mockMvc.perform(post("/comments/add"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/singlePost/" +
                        commentDTO.getPostID()));
        verify(commentService).addOrUpdateComment(comment);
    }

    @Test
    @DisplayName("Expect status is Redirection, redirected url is" +
            "/posts/singlePost/comment.getPost().getId() " +
            "and commentService used method deleteComment " +
            "with parameter comment")
    public void testDeleteComment() throws Exception {
        when(commentService.getCommentByID(comment.getId()))
                .thenReturn(ofNullable(comment));
        //then
        mockMvc.perform(get("/comments/delete/" + comment.getId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/singlePost/" +
                        comment.getPost().getId()));
        verify(commentService).deleteComment(comment);
    }

    private Comment createComment() {
        Member author = Member.builder()
                .name("Pushkin")
                .id(SOME_ID_33)
                .build();
        Post post = Post.builder()
                .id(SOME_ID_44)
                .author(author)
                .build();
        Member commentAuthor = Member.builder()
                .id(SOME_ID_44)
                .name("commentAuthorName")
                .build();

        return Comment.builder()
                .id(SOME_ID_22)
                .author(commentAuthor)
                .post(post)
                .text("PostText")
                .build();
    }

    private CommentDTO createCommentDto() {
        commentDTO = new CommentDTO();
        commentDTO.setId(SOME_ID_22);
        commentDTO.setAuthorProfileId(SOME_ID_44);
        commentDTO.setAuthorName("commentAuthorName");
        commentDTO.setPostID(comment.getPost().getId());
        commentDTO.setText(comment.getText());

        return commentDTO;
    }
}
