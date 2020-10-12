package com.ra.course.linkedin.model.entity.other;

import com.ra.course.linkedin.model.entity.BaseEntity;
import com.ra.course.linkedin.model.entity.person.Member;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Message extends BaseEntity {
    private List<Member> sendTo;
    private String messageBody;
    private Member author;
}
