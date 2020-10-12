package com.ra.course.linkedin.web.dto.message;

import com.ra.course.linkedin.web.dto.member.ContactDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageDto {
    private Long id;
    private Long authorId;
    private String messageBody;
    private String authorName;
    private List<ContactDto> sentTo;
}
