package com.ra.course.linkedin.model.entity.person;

import com.ra.course.linkedin.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Address extends BaseEntity {
    private String streetAddress;
    private String city;
    private String state;
    private String zipcode;
    private String country;
}
