package com.ra.course.linkedin.persistence.invitation;

import com.ra.course.linkedin.model.entity.other.ConnectionInvitation;
import com.ra.course.linkedin.persistence.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class InvitationRepositoryImpl extends BaseRepositoryImpl<ConnectionInvitation> implements InvitationRepository {

    private transient long currentId = 1L;

    public ConnectionInvitation save(final ConnectionInvitation connection) {
        final Optional<ConnectionInvitation> invitation = repository.stream().filter(connect ->
                connect.getAuthor().equals(connection.getAuthor()) &&
                        connect.getSentTo().equals(connection.getSentTo()) &&
                        connection.getAuthor().getContacts().contains(connect.getSentTo()) ||
                        connection.getSentTo().equals(connect.getSentTo()) &&
                                connection.getAuthor().equals(connect.getAuthor())
        ).findAny();

        return invitation.orElseGet(() -> {
            connection.setId(getAndIncreaseCurrentId());
            repository.add(connection);
            return connection;
        });
    }

    private long getAndIncreaseCurrentId() {
        return currentId++;
    }

}
