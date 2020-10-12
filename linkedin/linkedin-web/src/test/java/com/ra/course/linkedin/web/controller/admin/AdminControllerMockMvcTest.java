package com.ra.course.linkedin.web.controller.admin;

import com.ra.course.linkedin.model.entity.person.Account;
import com.ra.course.linkedin.model.entity.person.Admin;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.service.admin.AdminService;
import com.ra.course.linkedin.web.utils.ControllerUtils;
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
public class AdminControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdminService adminService;
    @MockBean
    private ControllerUtils controllerUtils;

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = createAdmin();
    }

    @Test
    @DisplayName("Expect status is Ok and model has attribute adminForm")
    public void addNewAdmin_getTest() throws Exception {
        //then
        mockMvc.perform(get("/admins/register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("adminForm"))
                .andExpect(view().name("admin/login"));
    }

    @Test
    @DisplayName("Expect status is Ok and model has attribute adminForm")
    public void adminLoginPageTest() throws Exception {
        //then
        mockMvc.perform(get("/admins"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/login"));
    }

    @Test
    @DisplayName("Expect status is redirection, redirected url is /")
    public void testLoginAdminWithoutEmail() throws Exception {
        //then
        mockMvc.perform(post("/admins/login")
                .param("email", "")
                .param("password", admin.getAccount().getPassword()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admins"));

        verify(adminService).login("", admin.getAccount().getPassword());
    }

    @Test
    @DisplayName("Expect status is redirection, redirected url is /")
    public void testLoginAdminWithoutPassword() throws Exception {
        //then
        mockMvc.perform(post("/admins/login")
                .param("email", admin.getEmail())
                .param("password", ""))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admins"));
        verify(adminService).login(admin.getEmail(), "");
    }

    @Test
    @DisplayName("Expect status is redirection, redirected url is /")
    public void testLoginAdmin() throws Exception {
        //when
        when(adminService.login(admin.getEmail(), admin.getAccount().getPassword())).thenReturn(ofNullable(admin));
        //then
        mockMvc.perform(post("/admins/login")
                .param("email", admin.getEmail())
                .param("password", admin.getAccount().getPassword()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admins/" + admin.getId()));
        verify(adminService).login(admin.getEmail(),
                admin.getAccount().getPassword());
    }

    @Test
    @DisplayName("Expect status is Redirection, redirected url is" +
            " and adminService used method save")
    public void addNewAdmin_postTest() throws Exception {
        //then
        mockMvc.perform(post("/admins/register")
                .param("name", "name")
                .param("email", "email")
                .param("password", "password"))
                .andExpect(status().is3xxRedirection());

        verify(adminService).save(any(Admin.class));
    }

    @Test
    @DisplayName("Expect status is Ok, model has attribute admins and " +
            "adminService used method getAllAdmins")
    public void getAllAdminsTest() throws Exception {
        //then
        mockMvc.perform(get("/admins/get-all-admins"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("admins"))
                .andExpect(view().name("admin/admins-list"));

        verify(adminService).getAllAdmins();
    }

    @Test
    @DisplayName("Expect status is Ok and model has attribute admin.")
    public void getAdminPageTest() throws Exception {
        //when
        when(controllerUtils.getAdminById(admin.getId())).thenReturn(admin);
        //then
        mockMvc.perform(get("/admins/" + admin.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("admin"))
                .andExpect(view().name("admin/admin-page"));
    }

    @Test
    @DisplayName("Expect status is Redirection and " +
            "redirected url is: /admins/{admin.getId()}; " +
            "adminService used method blockMember with parameter member")
    public void blockMemberTest() throws Exception {
        //when
        Member member = createMember();
        when(controllerUtils.getMemberById(member.getId())).thenReturn(member);
        //then
        mockMvc.perform(post("/admins/" + admin.getId() + "/block-member")
                .param("memberId", String.valueOf(member.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admins/" + admin.getId()));

        verify(adminService).blockMember(member);
    }

    @Test
    @DisplayName("Expect status is Redirection and " +
            "redirected url is: /admins/{admin.getId()}; " +
            "adminService used method unblockMember with parameter member")
    public void unblockMemberTest() throws Exception {
        //when
        Member member = createMember();
        when(controllerUtils.getMemberById(member.getId())).thenReturn(member);
        //then
        mockMvc.perform(post("/admins/" + admin.getId() +
                "/unblock-member")
                .param("memberId", String.valueOf(member.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admins/" + admin.getId()));

        verify(adminService).unblockMember(member);
    }

    private Admin createAdmin() {
        Admin admin = new Admin();
        admin.setId(SOME_ID_22);
        admin.setAccount(Account.builder()
                .password("12345")
                .build());
        admin.setEmail("member@mail.ru");
        return admin;
    }

    private Member createMember() {
        return Member.builder().id(SOME_ID_33).build();
    }
}
