package com.ra.course.linkedin.web.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContactDto {
    private Long id;
    private Long profileId;
    private String name;
}
