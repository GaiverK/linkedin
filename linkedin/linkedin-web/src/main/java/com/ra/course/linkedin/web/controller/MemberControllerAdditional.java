package com.ra.course.linkedin.web.controller;

import com.ra.course.linkedin.model.entity.other.Message;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.service.invitation.InvitationService;
import com.ra.course.linkedin.service.job.JobService;
import com.ra.course.linkedin.service.member.MemberService;
import com.ra.course.linkedin.web.dto.member.MemberRegistrationDto;
import com.ra.course.linkedin.web.dto.message.MessageSendDto;
import com.ra.course.linkedin.web.mapper.MessageMapper;
import com.ra.course.linkedin.web.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ra.course.linkedin.web.controller.MemberController.REDIRECT_MEMBERS;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberControllerAdditional {

    public static final String REDIR_PROFILES = "redirect:/profiles/get/";
    private final MemberService memberService;
    private final InvitationService invitationService;
    private final JobService jobService;
    private final ModelMapper modelMapper;
    private final MessageMapper messageMapper;
    private final ControllerUtils controllerUtils;

    @PostMapping("/login")
    public String login(@ModelAttribute("user") final MemberRegistrationDto memberRegistrDto) {
        final Optional<Member> member = memberService.login(memberRegistrDto.getEmail(), memberRegistrDto.getPassword());
        return member.map(user -> REDIR_PROFILES + user.getProfile().getId()).orElse("redirect:/");
    }

    @PostMapping(value = "/{id}/add-contact")
    public String addContact(@PathVariable("id") final Long id,
                             @RequestParam(value = "memberId") final Long memberId) {
        invitationService.acceptConnectionRequest(invitationService.createConnectionInvitation(id, memberId));
        return REDIRECT_MEMBERS + id;
    }

    @PostMapping(value = "/invitation")
    public String sendInvitation(@RequestParam final Long from,
                                 @RequestParam final Long to) {
        if (!from.equals(to)) {
            invitationService.sendConnectionRequest(invitationService.createConnectionInvitation(from, to));
        }
        return REDIRECT_MEMBERS + from;
    }

    @PostMapping(value = "/reject")
    public String rejectInvitation(@RequestParam final Long from,
                                   @RequestParam final Long to) {
        invitationService.rejectConnectionRequest(invitationService.createConnectionInvitation(from, to));
        return REDIRECT_MEMBERS + from;
    }

    @PostMapping(value = "/send-message")
    public String sendMessage(@RequestParam final Map<String, String> reqParam, @RequestParam("members[]") final List<String> membersIds,
                              @ModelAttribute("message") final MessageSendDto messageSendDto) {
        messageSendDto.setAuthorId(Long.parseLong(reqParam.get("authorId")));
        messageSendDto.setSentTo(String.join(",", membersIds));
        memberService.sendMessage(mapMessageDtoToMessage(messageSendDto));
        return REDIR_PROFILES + Long.parseLong(reqParam.get("profileId")) + "?tab=contact";
    }

    @PostMapping(value = "/{id}/delete-job")
    public String deleteJobFromSaved(@PathVariable("id") final Long id,
                                     @RequestParam("jobId") final Long jobId) {
        final Member member = controllerUtils.getMemberById(id);
        jobService.deleteJobFromSaved(member, controllerUtils.getJobById(jobId));
        return REDIR_PROFILES + member.getProfile().getId() + "?tab=sjobs";
    }

    private Message mapMessageDtoToMessage(final MessageSendDto dto) {
        if (modelMapper.getTypeMap(MessageSendDto.class, Message.class) == null) {
            modelMapper.addMappings(messageMapper);
        }
        return modelMapper.map(dto, Message.class);
    }
}
