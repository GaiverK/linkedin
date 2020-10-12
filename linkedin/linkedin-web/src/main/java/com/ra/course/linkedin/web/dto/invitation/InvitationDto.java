package com.ra.course.linkedin.web.dto.invitation;

import com.ra.course.linkedin.model.entity.other.ConnectionInvitationStatus;
import com.ra.course.linkedin.web.dto.member.MemberPageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InvitationDto {
    private Long id;
    private MemberPageDto author;
    private ConnectionInvitationStatus status;
}
