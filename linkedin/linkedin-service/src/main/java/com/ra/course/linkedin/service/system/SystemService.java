package com.ra.course.linkedin.service.system;

import com.ra.course.linkedin.model.entity.person.Member;

public interface SystemService {
    void sendMessageNotification(Member member);
    void sendConnPendingNotification(Member member);
    void sendNewSuggestNotification(Member member);
    void sendRecommendationNotification(Member member);
}
