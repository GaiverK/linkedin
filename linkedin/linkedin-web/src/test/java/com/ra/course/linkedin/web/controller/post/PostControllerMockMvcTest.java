package com.ra.course.linkedin.web.controller.post;

import com.ra.course.linkedin.model.entity.other.Comment;
import com.ra.course.linkedin.model.entity.other.Post;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.service.comment.CommentService;
import com.ra.course.linkedin.service.post.PostService;
import com.ra.course.linkedin.web.dto.comment.CommentDTO;
import com.ra.course.linkedin.web.dto.post.PostDto;
import com.ra.course.linkedin.web.mapper.CommentMapperUtil;
import com.ra.course.linkedin.web.mapper.PostMapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerMockMvcTest {

    public static final long SOME_ID_55 = 55L;

    @MockBean
    private PostService postService;
    @MockBean
    private PostMapperUtil postMapper;
    @MockBean
    private CommentService commentService;
    @MockBean
    private CommentMapperUtil commentMapper;

    @Autowired
    MockMvc mockMvc;

    private Post post;
    private PostDto postDto;
    private Comment comment;
    private CommentDTO commentDTO;

    @BeforeEach
    void setUp() {
        post = createPost();
        postDto = createPostDto();
        comment = createComment();
        commentDTO = createCommentDto();
    }

    @Test
    @DisplayName("Expect status is Ok and model has attribute postForm")
    public void testGetAddPost() throws Exception {
        //then
        mockMvc.perform(get("/posts/addNewPost"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("postForm"));
    }

    @Test
    @DisplayName("Expect status is Redirection, redirected url is " +
            "/posts/getAllPosts and postService used method addOrUpdatePost " +
            "with parameter post")
    public void testPostAddPost() throws Exception {
        //when
        when(postMapper.mapFromDtoToPost(any())).thenReturn(post);
        //then
        mockMvc.perform(post("/posts/addNewPost"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/getAllPosts"));

        verify(postService).addOrUpdatePost(post);
    }

    @Test
    @DisplayName("Expect status is Ok and model has attribute post; " +
            "postService used method getPostById with parameter post")
    public void testGetPost() throws Exception {
        //when
        when(postService.getPostById(post.getId())).thenReturn(ofNullable(post));
        when(postMapper.mapFromPostToDto(post)).thenReturn(postDto);
        //then
        mockMvc.perform(get("/posts/singlePost/" + post.getId() + "/update"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"));

        verify(postService).getPostById(post.getId());
    }

    @Test
    @DisplayName("Expect status is Redirection, redirected url is" +
            "/posts/getAllPosts " +
            "and postService used method addOrUpdatePost " +
            "with parameter post")
    public void testUpdatePost() throws Exception {
        //when
        when(postMapper.mapFromDtoToPost(any())).thenReturn(post);
        //then
        mockMvc.perform(post("/posts/update/" + post.getId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/getAllPosts"));

        verify(postService).addOrUpdatePost(post);
    }

    @Test
    @DisplayName("Expect status is Redirection, redirected url is" +
            "/posts/getAllPosts " +
            "and postService used method deletePost " +
            "with parameter post")
    public void testDeletePost() throws Exception {
        //when
        when(postService.getPostById(post.getId())).thenReturn(ofNullable(post));
        //then
        mockMvc.perform(get("/posts/delete/" + post.getId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/getAllPosts"));

        verify(postService).deletePost(post);
    }

    @Test
    @DisplayName("Expect status is Ok and model has attribute postList; " +
            "postService used method getAllPosts")
    public void testGetAllPosts() throws Exception {
        //then
        mockMvc.perform(get("/posts/getAllPosts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("postList"));

        verify(postService).getAllPosts();
    }

    @Test
    @DisplayName("Expect status is Ok and model has attribute postList; " +
            "postService used method getAllPosts")
    public void testViewAllPostsModel() throws Exception {
        //then
        mockMvc.perform(get("/posts/getAllPosts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("postList"));

        verify(postService).getAllPosts();
    }

    @Test
    @DisplayName("Expect status is Ok, model has attributes post, comments;" +
            "postService used method getPostById with parameter post")
    public void testViewAllPosts() throws Exception {
        //when
        when(postService.getPostById(post.getId())).thenReturn(ofNullable(post));
        when(postMapper.mapFromPostToDto(post)).thenReturn(postDto);
        when(commentService.getCommentsByPostID(post.getId()))
                .thenReturn(List.of(comment));
        when(commentMapper.map(comment)).thenReturn(commentDTO);
        //then
        mockMvc.perform(get("/posts/singlePost/" + post.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attributeExists("comments"));

        verify(postService).getPostById(post.getId());
    }

    private Post createPost() {
        Member author = Member.builder()
                .id(SOME_ID_33)
                .name("authorName")
                .build();

        return Post.builder()
                .id(SOME_ID_22)
                .author(author)
                .build();
    }

    private PostDto createPostDto() {
        postDto = new PostDto();
        postDto.setId(SOME_ID_22);

        return postDto;
    }

    private Comment createComment() {
        Member author = Member.builder()
                .id(SOME_ID_44)
                .name("commentAuthorName")
                .build();
        return Comment.builder()
                .id(SOME_ID_55)
                .author(author)
                .build();
    }

    private CommentDTO createCommentDto() {
        commentDTO = new CommentDTO();
        commentDTO.setId(SOME_ID_55);

        return commentDTO;
    }


}
