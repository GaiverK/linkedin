package com.ra.course.linkedin.persistence.member;

import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.BaseRepository;

import java.util.List;

public interface MemberRepository extends BaseRepository<Member> {
    void saveMembers(Iterable<Member> members);
    List<Member> searchMembersByName(final String name);
}
