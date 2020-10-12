package com.ra.course.linkedin.web.dto.group;

import com.ra.course.linkedin.web.dto.member.ContactDto;
import com.ra.course.linkedin.web.dto.member.MemberPageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GroupDto {
    private Long id;
    private Long ownerId;
    private String name;
    private String description;
    private String ownerName;
    private Long ownerProfileId;
    private List<MemberPageDto> groupMembers;

    private List<MemberPageDto> ownerContacts;
    private List<MemberPageDto> ownerFollowers;
    private List<MemberPageDto> ownerFollowingMembers;
}
