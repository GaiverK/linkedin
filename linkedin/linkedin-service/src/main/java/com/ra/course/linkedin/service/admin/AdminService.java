package com.ra.course.linkedin.service.admin;

import com.ra.course.linkedin.model.entity.person.Admin;
import com.ra.course.linkedin.model.entity.person.Member;

import java.util.List;
import java.util.Optional;

public interface AdminService {
    void blockMember(Member member);

    void unblockMember(Member member);

    Admin save(Admin admin);

    List<Admin> getAllAdmins();

    Optional<Admin> getAdminById(Long id);

    Optional<Admin> login(String email, String password);

    void deleteAdmin(Admin admin);
}
