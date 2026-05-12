package com.LMS.Library.Management.System.entities;

import com.LMS.Library.Management.System.enums.Gender;
import com.LMS.Library.Management.System.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String name;
    private String email;
    private String password;
    private LocalDate dob;
    private City city;
    private String address;
    private String contact;
    private String profilePic;
    private String verificationCode;
    @OneToMany(mappedBy = "user")
    private List<UserDocument> userDocuments;
    private String documentPath;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private UserType userType;
}
