package com.ra.course.linkedin.model.entity.other;

import com.ra.course.linkedin.model.entity.BaseEntity;
import com.ra.course.linkedin.model.entity.person.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Post extends BaseEntity {
    private final static int DEFAULT_TOTAL_LIKES = 0;
    private final static int DEFAULT_TOTAL_SHARES = 0;

    private String text;
    private int totalLikes;
    private int totalShares;
    private Member author;
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(Long id, String text, int totalLikes, int totalShares, Member author, List<Comment> comments) {
        super(id);
        this.text = text;
        this.totalLikes = totalLikes;
        this.totalShares = totalShares;
        this.author = author;
        this.comments = comments;
    }
}
