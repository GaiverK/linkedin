package com.ra.course.linkedin.service.admin;

import com.ra.course.linkedin.model.entity.person.AccountStatus;
import com.ra.course.linkedin.model.entity.person.Admin;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.persistence.admin.AdminRepository;
import com.ra.course.linkedin.persistence.member.MemberRepository;
import com.ra.course.linkedin.service.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final MemberRepository memberRepository;
    private final Utils utils;

    @Override
    public void blockMember(final Member member) {
        final Member blockMember = utils.findMember(member);
        blockMember.getAccount().setStatus(AccountStatus.BLOCKED);
        memberRepository.save(blockMember);
    }

    @Override
    public void unblockMember(final Member member) {
        final Member blockMember = utils.findMember(member);
        blockMember.getAccount().setStatus(AccountStatus.ACTIVE);
        memberRepository.save(blockMember);
    }

    @Override
    public Admin save(final Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return StreamSupport.stream(adminRepository.getAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Admin> getAdminById(final Long id) {
        return adminRepository.getById(id);
    }

    @Override
    public Optional<Admin> login(final String email, final String password) {
        return StreamSupport.stream(adminRepository.getAll().spliterator(), false).collect(Collectors.toList())
                .stream().filter(adminLog -> adminLog.getAccount().getPassword().equals(password) && adminLog.getEmail().equals(email)).findAny();
    }

    @Override
    public void deleteAdmin(final Admin admin) {
        adminRepository.delete(admin);
    }
}
