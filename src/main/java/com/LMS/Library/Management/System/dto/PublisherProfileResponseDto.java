package com.LMS.Library.Management.System.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublisherProfileResponseDto {

    private Integer publisherId;

    private String companyName;
    private String website;
    private String gstNumber;
    private String description;

    private Integer userId;
    private String name;
    private String email;
    private String contact;
    private String address;
    private String profilePic;

    private String city;
}
