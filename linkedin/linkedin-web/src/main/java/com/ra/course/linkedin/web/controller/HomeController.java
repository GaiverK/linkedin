package com.ra.course.linkedin.web.controller;

import com.ra.course.linkedin.service.company.CompanyService;
import com.ra.course.linkedin.service.member.MemberService;
import com.ra.course.linkedin.service.post.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@AllArgsConstructor
public class HomeController {

    public static final String INDEXX = "index";
    private final MemberService memberService;
    private final PostService postService;
    private final CompanyService companyService;

    @RequestMapping(value = {"/", INDEXX}, method = RequestMethod.GET)
    public String index(final Model model) {
        final Long membersCount = (long) memberService.getAllMembers().size();
        final Long postsCount = (long) postService.getAllPosts().size();
        final Long companiesCount = (long) companyService.getAllCompanies().size();

        model.addAttribute("membersCount", membersCount);
        model.addAttribute("postsCount", postsCount);
        model.addAttribute("companiesCount", companiesCount);
        return INDEXX;
    }
}
