package com.ra.course.linkedin.persistence.invitation;

import com.ra.course.linkedin.model.entity.other.ConnectionInvitation;
import com.ra.course.linkedin.persistence.BaseRepository;

public interface InvitationRepository extends BaseRepository<ConnectionInvitation> {
    @Override
    ConnectionInvitation save(ConnectionInvitation entity);
}
