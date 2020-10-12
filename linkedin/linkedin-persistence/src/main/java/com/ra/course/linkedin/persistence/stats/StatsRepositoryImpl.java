package com.ra.course.linkedin.persistence.stats;

import com.ra.course.linkedin.model.entity.profile.Stats;
import com.ra.course.linkedin.persistence.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository
public class StatsRepositoryImpl extends BaseRepositoryImpl<Stats> implements StatsRepository {
}
