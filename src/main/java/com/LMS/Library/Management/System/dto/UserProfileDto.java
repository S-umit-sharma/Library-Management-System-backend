package com.LMS.Library.Management.System.dto;

import com.LMS.Library.Management.System.enums.Gender;
import com.LMS.Library.Management.System.enums.Status;
import com.LMS.Library.Management.System.enums.UserType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileDto {

    private Integer userId;
    private String name;
    private String email;
    private LocalDate dob;
    private String address;
    private String contact;
    private String profilePic;

    private Long cityId;
    private String cityName;

    private Gender gender;
    private Status status;
    private UserType userType;
}