package com.ra.course.linkedin.model.entity.other;

import com.ra.course.linkedin.model.entity.BaseEntity;
import com.ra.course.linkedin.model.entity.person.Member;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Comment extends BaseEntity {
    private static final int DEFAULT_TOTAL_LIKES = 0;

    private String text;
    private int totalLikes;
    private Member author;
    private Post post;

    @Builder
    public Comment(Long id, String text, int totalLikes, Member author, Post post) {
        super(id);
        this.text = text;
        this.totalLikes = totalLikes;
        this.author = author;
        this.post = post;
    }
}
