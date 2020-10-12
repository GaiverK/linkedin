package com.ra.course.linkedin.model.entity.other;

import com.ra.course.linkedin.model.entity.BaseEntity;
import com.ra.course.linkedin.model.entity.person.Member;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConnectionInvitation extends BaseEntity {
    private static final ConnectionInvitationStatus DEFAULT_CONNECTION_INVITATION_STATUS = ConnectionInvitationStatus.PENDING;

    private LocalDate dateCreated = LocalDate.now();
    private ConnectionInvitationStatus status;
    private Member sentTo;
    private Member author;

    @Builder
    public ConnectionInvitation(Long id, ConnectionInvitationStatus status, Member sentTo, Member author) {
        super(id);
        this.status = status;
        this.sentTo = sentTo;
        this.author = author;
    }
}
