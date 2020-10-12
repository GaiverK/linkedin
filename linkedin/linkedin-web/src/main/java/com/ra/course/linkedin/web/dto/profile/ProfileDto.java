package com.ra.course.linkedin.web.dto.profile;

import com.ra.course.linkedin.model.entity.other.JobPosting;
import com.ra.course.linkedin.model.entity.person.AccountStatus;
import com.ra.course.linkedin.web.dto.member.MemberPageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfileDto {
    private long id;
    private String summary;
    private AccountStatus accountStatus;
    private String memberHeadline;
    private List<JobPosting> memberAppliedJobPosting;
    private List<JobPosting> memberSavedJobs;
    private List<MemberPageDto> memberFollowers;
    private List<MemberPageDto> memberFollowingMembers;
    private MemberPageDto member;

    private long memberId;
    private String memberName;
}
