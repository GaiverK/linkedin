package com.ra.course.linkedin.persistence.admin;

import com.ra.course.linkedin.model.entity.person.Admin;
import com.ra.course.linkedin.persistence.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository
public class AdminRepositoryImpl extends BaseRepositoryImpl<Admin> implements AdminRepository {
}
