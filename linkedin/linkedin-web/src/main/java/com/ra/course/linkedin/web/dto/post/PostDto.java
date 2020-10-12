package com.ra.course.linkedin.web.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostDto {
    private long id;
    private String text;
    private long profileId;
    private long authorId;
    private String authorName;
    private int commentsSize;
}
