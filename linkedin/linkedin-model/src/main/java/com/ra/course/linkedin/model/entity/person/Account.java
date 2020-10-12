package com.ra.course.linkedin.model.entity.person;

import com.ra.course.linkedin.model.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Account extends BaseEntity {

    private AccountStatus status;
    private String password;

    @Builder
    public Account(AccountStatus accountStatus, Long accountId, String password) {
        super(accountId);
        this.password = password;
        this.status = accountStatus;
    }
}
