package com.ra.course.linkedin.model.entity.profile;

import com.ra.course.linkedin.model.entity.BaseEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Stats extends BaseEntity {

    private long totalViews;
    private int totalConnections;
    private int searchAppearances;
    private Profile profile;

    @Builder
    public Stats(Long id, long totalViews, int totalConnections, int searchAppearances,
                 Profile profile) {
        super(id);
        this.totalViews = totalViews;
        this.totalConnections = totalConnections;
        this.searchAppearances = searchAppearances;
        this.profile = profile;
    }
}
