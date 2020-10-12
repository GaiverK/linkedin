package com.ra.course.linkedin.web.utils;

import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.service.member.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

@Controller
public class FileUploader {

    public static final String UPLOAD = "/upload";
    private static final Logger LOGGER = Logger.getLogger(FileUploader.class.getName());
    private final transient MemberService memberService;

    public FileUploader(final MemberService memberService) {
        this.memberService = memberService;
    }

    @RequestMapping(value = UPLOAD, method = RequestMethod.GET)
    public String provideUploadInfo() {
        return "formupload";
    }

    @RequestMapping(value = UPLOAD, method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") final MultipartFile file, @RequestParam final Long member_id) {
        final String name = file.getOriginalFilename();
        final Member member = memberService.getMemberById(member_id).get();
        try {
            final BufferedImage image = ImageIO.read(file.getInputStream());
            ImageIO.write(image, "jpg", new File("./linkedin/linkedin-web/src/main/resources/static/upload/" + name));
            member.setPhoto("/upload/" + name);
            memberService.save(member);
        } catch (IOException e) {
            LOGGER.severe("Image was not uploaded");
        }
        return "redirect:/profiles/get/" + member.getProfile().getId();
    }

}