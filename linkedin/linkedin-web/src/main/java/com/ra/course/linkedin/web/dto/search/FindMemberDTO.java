package com.ra.course.linkedin.web.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FindMemberDTO {
    private Long id;
    private String headline;
    private LocalDate dateOfMembership;
    private String name;
    private String email;

    private long profileId;
}
