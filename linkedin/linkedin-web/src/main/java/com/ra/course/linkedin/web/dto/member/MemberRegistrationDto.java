package com.ra.course.linkedin.web.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberRegistrationDto {
    private String name;
    private String email;
    private String password;
}
