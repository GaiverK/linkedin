package com.ra.course.linkedin.service.member;

import com.ra.course.linkedin.model.entity.other.Company;
import com.ra.course.linkedin.model.entity.other.Message;
import com.ra.course.linkedin.model.entity.person.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    void followCompany(Member member, Company company);
    void unFollowCompany(Member member, Company company);
    void followMember(Member member, Member followingMember);
    void unFollowMember(Member member, Member unFollowingMember);
    void addContact(Member fromWhom, Member toWhom);
    void sendMessage(Message message);
    Optional<Member> login(String email, String password);
    Member save(Member member);
    List<Member> getAllMembers();
    Optional<Member> getMemberById(Long id);
    void deleteMember(Member member);
}
