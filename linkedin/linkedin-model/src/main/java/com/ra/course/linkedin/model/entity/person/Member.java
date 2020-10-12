package com.ra.course.linkedin.model.entity.person;

import com.ra.course.linkedin.model.entity.notification.NotificationMethod;
import com.ra.course.linkedin.model.entity.other.*;
import com.ra.course.linkedin.model.entity.profile.Profile;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Member extends Person {

     private LocalDate dateOfMembership = LocalDate.now();
     private String headline;
    private String photo;
    private Profile profile;

     private List<Member> contacts = new ArrayList<>();
     private List<Member> followers = new ArrayList<>();
     private List<Member> followingMembers = new ArrayList<>();
     private List<Company> followingCompanies = new ArrayList<>();
     private List<Group> joinedGroups = new ArrayList<>();
     private List<Group> createdGroups = new ArrayList<>();
     private List<JobPosting> appliedJobPosting = new ArrayList<>();
     private List<JobPosting> savedJobs = new ArrayList<>();
     private List<Comment> memberComments = new ArrayList<>();
     private List<Post> createdPosts = new ArrayList<>();
     private List<Message> sentMessages = new ArrayList<>();
     private List<Message> incomingMessages = new ArrayList<>();
     private List<ConnectionInvitation> sentConnectionInvitations = new ArrayList<>();
     private List<ConnectionInvitation> incomingConnectionInvitations = new ArrayList<>();


     @Builder
     public Member(Long id, String name, Address address, String email, String phone,
                   Account account, NotificationMethod[] notificationMethods,
                   Profile profile) {
          super(id, name, address, email, phone, account, notificationMethods);
          this.profile = profile;
     }
}
