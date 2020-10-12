package com.ra.course.linkedin.persistence.member;

import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.configuration.RepositoryConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.ra.course.linkedin.persistence.PersistenceIntegrationTestUtils.tryToDelete;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = RepositoryConfiguration.class)
class MemberRepositoryImplIntegrationTest {

    @Autowired
    MemberRepository memberRepository;

    private List<Member> members;

    @BeforeEach
    public void before() {
        members = new LinkedList<>();
    }

    @AfterEach
    public void after() {
        members.forEach(member -> tryToDelete(memberRepository, member));
    }

    @Test
    @DisplayName("When the members are updated, " +
            "then they must be changed in repository")
    void testUpdateMembers() {
        //given
        members = List.of(new Member(), new Member(), new Member());
        members.forEach(memberRepository::save);
        //when
        List<Member> changedMembers = changeMembers(members);
        memberRepository.saveMembers(changedMembers);
        List<Member> result = (List<Member>) memberRepository.getAll();
        Member member0 = result.get(0);
        Member member1 = result.get(1);
        Member member2 = result.get(2);
        //then
        assertAll(
                () -> assertEquals(3, result.size()),
                () -> assertEquals("Adam1", member0.getName()),
                () -> assertEquals("Adam2", member1.getName()),
                () -> assertEquals("Adam3", member2.getName())
        );
    }

    @Test
    @DisplayName("When searched members exist, then must be returned their list")
    void testWhenSearchedMembersExist() {
        //given
        String searchedName = "aAa";
        //when
        List<Member> foundMembers = getSearchedMembers(searchedName);
        //then
        assertAll(
                () -> assertEquals(foundMembers.size(), 2),
                () -> assertEquals(foundMembers.get(0).getName(),
                        createSearchedMembers().get(0).getName()),
                () -> assertEquals(foundMembers.get(1).getName(),
                        createSearchedMembers().get(2).getName())
        );
    }

    @Test
    @DisplayName("When searched members do not exist, " +
            "then must be returned empty list")
    void testWhenSearchedMembersDoNotExist() {
        //given
        String searchedName = "bBb";
        //when
        List<Member> foundMembers = getSearchedMembers(searchedName);
        //then
        assertEquals(foundMembers.size(), 0);
    }

    private List<Member> changeMembers(List<Member> memberList) {
        List<Member> list = new ArrayList<>();
        int i = 1;
        for (Member member : memberList) {
            member.setName(
                    "Adam".concat(String.valueOf(i)));
            list.add(member);
            i++;
        }
        return list;
    }

    private List<Member> createSearchedMembers() {
        List<Member> searchedMembers = new ArrayList<>();

        Member member = Member.builder()
                .name("aAaA")
                .build();
        Member member1 = Member.builder()
                .name("AbAa")
                .build();
        Member member2 = Member.builder()
                .name("BAaAb")
                .build();

        searchedMembers.add(member);
        searchedMembers.add(member1);
        searchedMembers.add(member2);

        return searchedMembers;
    }

    private List<Member> getSearchedMembers(String searchedName) {
        members = createSearchedMembers();
        memberRepository.saveMembers(members);
        return memberRepository.searchMembersByName(searchedName);
    }
}