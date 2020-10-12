package com.ra.course.linkedin.service.member;

import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.model.entity.other.Message;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.company.CompanyRepository;
import com.ra.course.linkedin.persistence.member.MemberRepository;
import com.ra.course.linkedin.service.system.SystemService;
import com.ra.course.linkedin.service.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final Utils utils;

    private final SystemService systemService;

    @Override
    public void followCompany(final Member member, final Company company) {
        final Member updatedMember = utils.findMember(member);
        if (!updatedMember.getFollowingCompanies().contains(company)) {
            updatedMember.getFollowingCompanies().add(company);
            memberRepository.save(updatedMember);
        }

        final Company updatedCompany = utils.findCompany(company);
        if (!updatedCompany.getFollowers().contains(member)) {
            updatedCompany.getFollowers().add(member);
            companyRepository.save(updatedCompany);
        }
    }

    @Override
    public void unFollowCompany(final Member member, final Company company) {
        final Member updatedMember = utils.findMember(member);
        updatedMember.getFollowingCompanies().remove(company);
        memberRepository.save(updatedMember);

        final Company updatedCompany = utils.findCompany(company);
        updatedCompany.getFollowers().remove(member);
        companyRepository.save(updatedCompany);
    }

    @Override
    public void followMember(final Member member, final Member followingMember) {
        final Member updatedMember = utils.findMember(member);
        if (!updatedMember.getFollowingMembers().contains(followingMember)) {
            updatedMember.getFollowingMembers().add(followingMember);
            memberRepository.save(updatedMember);
        }
        final Member updFollMember = utils.findMember(followingMember);
        if (!updFollMember.getFollowers().contains(updatedMember)) {
            updFollMember.getFollowers().add(updatedMember);
            memberRepository.save(updFollMember);
        }
    }

    @Override
    public void unFollowMember(final Member member, final Member unFollowingMember) {
        final Member updatedMember = utils.findMember(member);
        final Member updUnFollMember = utils.findMember(unFollowingMember);

        updatedMember.getFollowingMembers().remove(updUnFollMember);
        memberRepository.save(updatedMember);

        updUnFollMember.getFollowers().remove(updatedMember);
        memberRepository.save(updUnFollMember);
    }

    @Override
    public void addContact(final Member toWhom, final Member whom) {
        final Member participant = utils.findMember(toWhom);
        if (!participant.getContacts().contains(whom)) {
            participant.getContacts().add(whom);
            memberRepository.save(participant);
        }
    }

    @Override
    public void sendMessage(final Message message) {
        final Member sender = message.getAuthor();

        sender.getSentMessages().add(message);
        sender.getIncomingMessages().add(message);
        memberRepository.save(sender);

        final List<Member> memberList =
                message.getSendTo().stream()
                        .peek((Member member) -> {
                            if (!member.getId().equals(sender.getId())) {
                                member.getIncomingMessages().add(message);
                                systemService.sendMessageNotification(member);
                            }
                        })
                        .collect(Collectors.toList());

        memberRepository.saveMembers(memberList);
    }

    @Override
    public Optional<Member> login(final String email, final String password) {
        return StreamSupport
                .stream(memberRepository.getAll().spliterator(), false)
                .filter(memberlog ->
                        memberlog.getAccount().getPassword().equals(password) &&
                                memberlog.getEmail().equals(email))
                .findAny();
    }

    @Override
    public Member save(final Member member) {
        return memberRepository.save(member);
    }

    @Override
    public List<Member> getAllMembers() {
        return StreamSupport.stream(memberRepository.getAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Member> getMemberById(final Long id) {
        return memberRepository.getById(id);
    }

    @Override
    public void deleteMember(final Member member) {
        memberRepository.delete(member);
    }
}
