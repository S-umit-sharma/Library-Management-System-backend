package com.LMS.Library.Management.System.dto;

import com.LMS.Library.Management.System.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileUpdateDto {

    private String name;
    private LocalDate dob;
    private String address;
    private String contact;

    private Long cityId;

    private Gender gender;

}