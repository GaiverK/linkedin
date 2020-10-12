package com.ra.course.linkedin.web.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDTO {
    private Long id;
    private String text;
    private int totalLikes;
    private Long authorProfileId;
    private Long authorID;
    private String authorName;
    private Long postID;
}
