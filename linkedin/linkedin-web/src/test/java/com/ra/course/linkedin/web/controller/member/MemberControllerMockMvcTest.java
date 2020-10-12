package com.ra.course.linkedin.web.controller.member;

import com.ra.course.linkedin.model.entity.notification.NotificationMethod;
import com.ra.course.linkedin.model.entity.other.*;
import com.ra.course.linkedin.model.entity.person.Account;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.persistence.invitation.InvitationRepository;
import com.ra.course.linkedin.service.job.JobService;
import com.ra.course.linkedin.service.member.MemberService;
import com.ra.course.linkedin.web.dto.message.MessageSendDto;
import com.ra.course.linkedin.web.mapper.MessageMapper;
import com.ra.course.linkedin.web.utils.ControllerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.ra.course.linkedin.model.entity.other.ConnectionInvitationStatus.PENDING;
import static com.ra.course.linkedin.web.controller.post.PostControllerMockMvcTest.SOME_ID_55;
import static com.ra.course.linkedin.web.controller.profile.ProfileControllerMockMvcTest.SOME_ID_22;
import static com.ra.course.linkedin.web.controller.recommendation.RecommendationControllerMockMvcTest.SOME_ID_33;
import static com.ra.course.linkedin.web.controller.recommendation.RecommendationControllerMockMvcTest.SOME_ID_44;
import static java.util.Optional.ofNullable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerMockMvcTest {

    public static final String MEMBER_MAIL_RU = "member@mail.ru";
    public static final String PASSWORD = "12345";
    @MockBean
    private MemberService memberService;
    @MockBean
    private JobService jobService;
    @MockBean
    private InvitationRepository invitationRepository;
    @MockBean
    private ControllerUtils controllerUtils;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MessageMapper messageMapper;

    private Member member;
    private ConnectionInvitation invitation;
    private Member secondMember;
    private Company company;
    private JobPosting job;

    @BeforeEach
    void setUp() {
        member = createMember(SOME_ID_22);
        secondMember = createMember(SOME_ID_44);
        invitation = createInvitation();
        company = createCompany();
        job = createJob();
    }

    @Test
    @DisplayName("Expect redirected url to /members/{member.id}")
    public void sendInvitation() throws Exception {
        //when
        long memberId = member.getId();
        when(memberService.getMemberById(member.getId())).thenReturn(ofNullable(member));
        when(memberService.getMemberById(secondMember.getId())).thenReturn(ofNullable(secondMember));
        when(invitationRepository.save(any(ConnectionInvitation.class))).thenReturn(invitation);
        //then
        mockMvc.perform(post("/members/invitation")
                .param("from", String.valueOf(member.getId()))
                .param("to", String.valueOf(secondMember.getId()))
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members/" + memberId));
    }

    @Test
    @DisplayName("Expect redirected url to /members/{member.id}")
    public void rejectInvitation() throws Exception {
        //when
        long memberId = member.getId();
        when(memberService.getMemberById(member.getId())).thenReturn(ofNullable(member));
        when(memberService.getMemberById(secondMember.getId())).thenReturn(ofNullable(secondMember));
        when(invitationRepository.save(any(ConnectionInvitation.class))).thenReturn(invitation);
        when(invitationRepository.getById(anyLong())).thenReturn(ofNullable(invitation));
        //then
        mockMvc.perform(post("/members/reject")
                .param("from", String.valueOf(member.getId()))
                .param("to", String.valueOf(secondMember.getId()))
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members/" + memberId));
    }

    @Test
    @DisplayName("Member can't send invitation to himself")
    public void sendInvitationToHimSelf() throws Exception {
        //then
        mockMvc.perform(post("/members/invitation")
                .param("from", String.valueOf(member.getId()))
                .param("to", String.valueOf(member.getId()))
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members/" + member.getId()));
    }

    @Test
    @DisplayName("Expect status is Redirection" +
            " and memberService used method save")
    public void testPostRegisterNewMember() throws Exception {
        //then
        mockMvc.perform(post("/members/register-new-member")
                .param("password", "password"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());

        verify(memberService).save(any(Member.class));
    }

    @Test
    @DisplayName("Expect status is Ok, model has attribute members and " +
            "memberService used method getAllMembers")
    public void testGetAllMembers() throws Exception {
        //when
        when(memberService.getAllMembers()).thenReturn(List.of(member));
        //then
        mockMvc.perform(get("/members/get-all-members"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("members"));

        verify(memberService).getAllMembers();
    }

    @Test
    @DisplayName("Expect status is Ok and model has attribute updateMember")
    public void testGetUpdateMember() throws Exception {
        //when
        when(controllerUtils.getMemberById(member.getId())).thenReturn(member);
        //then
        mockMvc.perform(get("/members/update-member/" + member.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("updateMember"));
    }

    @Test
    @DisplayName("Expect status is Redirection and " +
            "redirected url is /members/get-all-members;" +
            "member was saved")
    public void testPostUpdateMember() throws Exception {
        //when
        when(controllerUtils.getMemberById(member.getId())).thenReturn(member);
        //then
        mockMvc.perform(post("/members/update-member/" + member.getId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members/get-all-members"));

        verify(memberService).save(member);
    }

    @Test
    @DisplayName("Expect status is Redirection and " +
            "redirected url is: /members/{member.getId()}; " +
            "memberService used method followCompany with parameters: " +
            "member, company")
    public void testFollowCompany() throws Exception {
        //when
        when(controllerUtils.getMemberById(member.getId())).thenReturn(member);
        when(controllerUtils.getCompanyById(company.getId())).thenReturn(company);
        //then
        mockMvc.perform(
                post("/members/" + member.getId() + "/follow-company")
                        .param("companyId", String.valueOf(company.getId())))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members/" + member.getId()));

        verify(memberService).followCompany(member, company);
    }

    @Test
    @DisplayName("Expect status is Redirection and " +
            "redirected url is: /members/{member.getId()}; " +
            "memberService used method unFollowCompany with parameters: " +
            "member, company")
    public void testUnFollowCompany() throws Exception {
        //when
        when(controllerUtils.getMemberById(member.getId())).thenReturn(member);
        when(controllerUtils.getCompanyById(company.getId())).thenReturn(company);
        //then
        mockMvc.perform(
                post("/members/" + member.getId() + "/unfollow-company")
                        .param("companyId", String.valueOf(company.getId())))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members/" + member.getId()));

        verify(memberService).unFollowCompany(member, company);
    }

    @Test
    @DisplayName("Expect status is Redirection and " +
            "redirected url is: /members/{member.getId()}; " +
            "memberService used method followMember with parameters: " +
            "member, secondMember")
    public void testFollowMember() throws Exception {
        //when
        when(controllerUtils.getMemberById(member.getId())).thenReturn(member);
        when(controllerUtils.getMemberById(secondMember.getId()))
                .thenReturn(secondMember);
        //then
        mockMvc.perform(post("/members/" + member.getId() +
                "/follow-member")
                .param("memberId", String.valueOf(secondMember.getId())))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members/" + member.getId()));

        verify(memberService).followMember(member, secondMember);
    }

    @Test
    @DisplayName("Expect status is Redirection and " +
            "redirected url is: /members/{member.getId()}; " +
            "memberService used method unFollowMember with parameters: " +
            "member, secondMember")
    public void testUnFollowMember() throws Exception {
        //when
        when(controllerUtils.getMemberById(member.getId())).thenReturn(member);
        when(controllerUtils.getMemberById(secondMember.getId()))
                .thenReturn(secondMember);
        //then
        mockMvc.perform(post("/members/" + member.getId() +
                "/unfollow-member")
                .param("memberId", String.valueOf(secondMember.getId())))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members/" + member.getId()));

        verify(memberService).unFollowMember(member, secondMember);
    }

    @Test
    @DisplayName("Expect status is redirection, redirected url is /")
    public void testLoginMemberWithoutEmail() throws Exception {
        //then
        mockMvc.perform(post("/members/login")
                .param("email", "")
                .param("password", member.getAccount().getPassword()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(memberService).login("", member.getAccount().getPassword());
    }

    @Test
    @DisplayName("Expect status is redirection, redirected url is /")
    public void testLoginMemberWithoutPassword() throws Exception {
        //then
        mockMvc.perform(post("/members/login")
                .param("email", member.getEmail())
                .param("password", ""))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        verify(memberService).login(member.getEmail(), "");
    }

    @Test
    @DisplayName("Expect status is redirection, redirected url is /")
    public void testLoginMember() throws Exception {
        //when
        when(memberService.login(member.getEmail(), member.getAccount().getPassword())).thenReturn(ofNullable(member));
        //then
        mockMvc.perform(post("/members/login")
                .param("email", member.getEmail())
                .param("password", member.getAccount().getPassword()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profiles/get/" + member.getProfile().getId()));
        verify(memberService).login(member.getEmail(),
                member.getAccount().getPassword());
    }

    @Test
    @DisplayName("Expect status is Redirection and " +
            "redirected url is: /members/{member.getId()}; " +
            "memberService used method addContact with parameters: " +
            "member, secondMember")
    public void testAddContact() throws Exception {
        //when
        when(invitationRepository.save(any(ConnectionInvitation.class))).thenReturn(invitation);
        when(invitationRepository.getById(anyLong())).thenReturn(ofNullable(invitation));
        when(memberService.getMemberById(member.getId())).thenReturn(ofNullable(member));
        when(memberService.getMemberById(secondMember.getId()))
                .thenReturn(ofNullable(secondMember));

        //then
        mockMvc.perform(post("/members/" + member.getId() +
                "/add-contact")
                .param("memberId", String.valueOf(secondMember.getId())))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members/" + member.getId()));

        verify(memberService).addContact(member, secondMember);
    }

    @Test
    @DisplayName("Expect status is Redirection and " +
            "redirected url is: /profiles/get/{member.getProfile().getId()} + ?tab=contact; ")
    public void testSendMessage() throws Exception {
        //then
        mockMvc.perform(post("/members/send-message")
                .param("authorId", String.valueOf(member.getId()))
                .param("profileId", String.valueOf(member.getProfile().getId()))
                .param("members[]", "1,2,3,4")
        )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profiles/get/" + member.getProfile().getId() + "?tab=contact"));

        verify(memberService).sendMessage(any(Message.class));
    }

    @Test
    @DisplayName("Expect status is Redirection and " +
            "redirected url is: /profiles/get/{member.getProfile().getId()}?tab=sjobs; " +
            "jobService used method deleteJob with parameters: " +
            "member, job")
    public void testDeleteJob() throws Exception {
        //when
        when(controllerUtils.getMemberById(member.getId())).thenReturn(member);
        when(controllerUtils.getJobById(job.getId())).thenReturn(job);
        //then
        mockMvc.perform(post("/members/" + member.getId() + "/delete-job")
                .param("jobId", String.valueOf(job.getId())))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profiles/get/" + member.getProfile().getId() + "?tab=sjobs"));

        verify(jobService).deleteJobFromSaved(member, job);
    }

    @Test
    @DisplayName("Test repeated mapping for modelMapper")
    public void testMapMessageDtoToMessage() throws Exception {
        if (modelMapper.getTypeMap(MessageSendDto.class, Message.class) == null) {
            modelMapper.addMappings(messageMapper);
        }
        mockMvc.perform(post("/members/" + member.getId() +
                "/send-message"));
    }

    private Member createMember(long id) {

        return Member.builder()
                .id(id)
                .notificationMethods(NotificationMethod.values())
                .profile(Profile.builder()
                        .id(id)
                        .build())
                .email(MEMBER_MAIL_RU)
                .account(Account.builder()
                        .password(PASSWORD)
                        .build())
                .build();
    }

    private Company createCompany() {
        return Company.builder()
                .id(SOME_ID_33)
                .build();
    }

    private ConnectionInvitation createInvitation() {
        return ConnectionInvitation.builder()
                .id(SOME_ID_33)
                .status(PENDING)
                .author(member)
                .sentTo(secondMember)
                .build();
    }

    private JobPosting createJob() {
        return JobPosting.builder()
                .id(SOME_ID_55)
                .build();
    }
}
