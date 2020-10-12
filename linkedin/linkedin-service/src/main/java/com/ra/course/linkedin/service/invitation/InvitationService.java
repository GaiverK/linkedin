package com.ra.course.linkedin.service.invitation;

import com.ra.course.linkedin.model.entity.other.ConnectionInvitation;

public interface InvitationService {
    ConnectionInvitation createConnectionInvitation(Long from, Long to);

    void sendConnectionRequest(ConnectionInvitation connectionInv);

    void acceptConnectionRequest(ConnectionInvitation connectionInv);

    void rejectConnectionRequest(ConnectionInvitation connectionInv);
}
