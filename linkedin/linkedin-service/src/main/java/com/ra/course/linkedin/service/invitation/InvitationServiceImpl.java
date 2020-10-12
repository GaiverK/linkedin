package com.ra.course.linkedin.service.invitation;

import com.ra.course.linkedin.model.entity.other.ConnectionInvitation;
import com.ra.course.linkedin.model.entity.other.ConnectionInvitationStatus;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.invitation.InvitationRepository;
import com.ra.course.linkedin.persistence.member.MemberRepository;
import com.ra.course.linkedin.service.member.MemberService;
import com.ra.course.linkedin.service.system.SystemService;
import com.ra.course.linkedin.service.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ra.course.linkedin.model.entity.other.ConnectionInvitationStatus.PENDING;

@Service
@AllArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepo;
    private final MemberRepository memberRepository;
    private final Utils utils;

    private final MemberService memberService;
    private final SystemService systemService;

    @Override
    public ConnectionInvitation createConnectionInvitation(final Long from, final Long to) {
        final ConnectionInvitation invitation = ConnectionInvitation.builder()
                .author(memberService.getMemberById(from).get())
                .sentTo(memberService.getMemberById(to).get())
                .status(PENDING)
                .build();

        return invitationRepo.save(invitation);
    }

    @Override
    public void sendConnectionRequest(final ConnectionInvitation invitation) {

        final Member sender = invitation.getAuthor();

        if (!sender.getSentConnectionInvitations().contains(invitation)) {
            sender.getSentConnectionInvitations().add(invitation);
            memberRepository.save(sender);

            final Member recipient = invitation.getSentTo();
            recipient.getIncomingConnectionInvitations().add(invitation);
            memberRepository.save(recipient);
            systemService.sendConnPendingNotification(recipient);
        }
    }

    @Override
    public void acceptConnectionRequest(final ConnectionInvitation invitation) {
        final ConnectionInvitation updInvitation = utils.findInvitation(invitation);
        final Member author = invitation.getAuthor();
        final Member recipient = invitation.getSentTo();

        updInvitation.setStatus(ConnectionInvitationStatus.ACCEPTED);

        invitationRepo.save(updInvitation);

        memberService.addContact(author, recipient);
        memberService.addContact(recipient, author);

        systemService.sendNewSuggestNotification(author);
        systemService.sendNewSuggestNotification(recipient);
    }

    @Override
    public void rejectConnectionRequest(final ConnectionInvitation invitation) {
        final ConnectionInvitation updInvitation = utils.findInvitation(invitation);

        updInvitation.setStatus(ConnectionInvitationStatus.REJECTED);

        invitationRepo.save(updInvitation);
    }
}
