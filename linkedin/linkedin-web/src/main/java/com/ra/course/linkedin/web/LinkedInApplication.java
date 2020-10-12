package com.ra.course.linkedin.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SuppressWarnings("PMD")
@SpringBootApplication(scanBasePackages = "com.ra.course.linkedin")
public class LinkedInApplication {
    public static void main(final String[] args) {
        SpringApplication.run(LinkedInApplication.class, args);
    }
}
