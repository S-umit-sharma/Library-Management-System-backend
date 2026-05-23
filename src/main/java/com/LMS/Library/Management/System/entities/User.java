package com.LMS.Library.Management.System.entities;

import com.LMS.Library.Management.System.enums.Gender;
import com.LMS.Library.Management.System.enums.Status;
import com.LMS.Library.Management.System.enums.UserType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String name;
    private String email;
    private String password;
    private LocalDate dob;
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
    private String address;
    private String contact;
    private String verificationCode;

    @OneToMany(mappedBy = "user")
    private List<UserDocument> userDocuments;

    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private UserType userType;
}
