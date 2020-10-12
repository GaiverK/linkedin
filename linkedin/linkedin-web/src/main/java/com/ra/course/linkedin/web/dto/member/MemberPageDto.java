package com.ra.course.linkedin.web.dto.member;

import com.ra.course.linkedin.model.entity.person.AccountStatus;
import com.ra.course.linkedin.web.dto.company.CompanyDto;
import com.ra.course.linkedin.web.dto.group.GroupDto;
import com.ra.course.linkedin.web.dto.invitation.InvitationDto;
import com.ra.course.linkedin.web.dto.job.JobPostingDto;
import com.ra.course.linkedin.web.dto.message.MessageDto;
import com.ra.course.linkedin.web.dto.profile.ProfileDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberPageDto {
    private String name;
    private String email;
    private String headline;
    private String photo;
    private AccountStatus accountStatus;
    private LocalDate dateOfMembership;
    private List<CompanyDto> followingCompanies;
    private List<MemberPageDto> contacts;
    private List<ContactDto> followers;
    private List<ContactDto> followingMembers;
    private List<MessageDto> sentMessages;
    private List<MessageDto> incomingMessages;
    private List<GroupDto> joinedGroups;
    private List<GroupDto> createdGroups;
    private List<JobPostingDto> appliedJobPosting;
    private List<JobPostingDto> savedJobs;
    private List<InvitationDto> incomingConnectionInvitations;
    private List<InvitationDto> sentConnectionInvitations;
    private long Id;
    private Long profileId;
}
