package com.ra.course.linkedin.service.utils;

import com.ra.course.linkedin.model.entity.exceptions.EntityNotFoundException;
import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.model.entity.other.ConnectionInvitation;
import com.ra.course.linkedin.model.entity.other.Post;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.company.CompanyRepository;
import com.ra.course.linkedin.persistence.invitation.InvitationRepository;
import com.ra.course.linkedin.persistence.member.MemberRepository;
import com.ra.course.linkedin.persistence.post.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Utils {

    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final InvitationRepository invitationRepo;
    private final PostRepository postRepository;

    public Member findMember(final Member member) {
        return memberRepository.getById(member.getId()).orElseThrow(
                () -> new EntityNotFoundException(Member.class.getSimpleName()));
    }

    public Company findCompany(final Company company) {
        return companyRepository.getById(company.getId()).orElseThrow(
                () -> new EntityNotFoundException(Company.class.getSimpleName()));
    }

    public ConnectionInvitation findInvitation(final ConnectionInvitation invitation) {
        return invitationRepo.getById(invitation.getId()).orElseThrow(
                () -> new EntityNotFoundException(
                        ConnectionInvitation.class.getSimpleName()));
    }

    public Post findPost(final Post post) {
        return postRepository.getById(post.getId()).orElseThrow(
                () -> new EntityNotFoundException(Post.class.getSimpleName()));
    }
}
