package com.LMS.Library.Management.System.entity;

import jakarta.persistence.Entity;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class User {
    private Integer userId;
    private String name;
    private String email;
    private String password;
    private Gender gender;
    private LocalDate dob;
    private City city;
    private String address;
    private String contact;
    private String profilePic;
    private String verificationCode;
    private Status status;
    private UserDocument userDocuments;
    private String documentPath;
    private UserType userType;
}
