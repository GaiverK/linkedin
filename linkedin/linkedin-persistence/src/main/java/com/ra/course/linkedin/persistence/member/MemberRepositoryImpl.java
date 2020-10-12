package com.ra.course.linkedin.persistence.member;

import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Repository
public class MemberRepositoryImpl extends BaseRepositoryImpl<Member>
        implements MemberRepository {

    @Override
    public void saveMembers(final Iterable<Member> members) {
        members.forEach(this::save);
    }

    @Override
    public List<Member> searchMembersByName(final String name) {
        return repository.stream()
                .filter(member -> member.getName().toLowerCase(Locale.ROOT)
                        .contains(name.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
    }
}
