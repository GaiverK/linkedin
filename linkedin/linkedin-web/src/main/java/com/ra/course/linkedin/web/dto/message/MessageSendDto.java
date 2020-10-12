package com.ra.course.linkedin.web.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageSendDto {
    private Long authorId;
    private String messageBody;
    private String sentTo;
}
