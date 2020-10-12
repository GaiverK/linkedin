package com.ra.course.linkedin.web.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberUpdateDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String headline;
}
