package com.ra.course.linkedin.persistence.profile;

import com.ra.course.linkedin.model.entity.profile.Profile;
import com.ra.course.linkedin.persistence.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository
public class ProfileRepositoryImpl extends BaseRepositoryImpl<Profile> implements ProfileRepository {
}
