package com.LMS.Library.Management.System.entities;

import com.LMS.Library.Management.System.enums.LibrarianProfileStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
public class Librarian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer librarianId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String highestQualification;

    private Double totalExperienceYears;

    private String specialization;

    @Column(length = 500)
    private String certifications;

    private String preferredDesignation;

    @Column(length = 1000)
    private String bio;

    private Boolean availableForHire = true;

    @Enumerated(EnumType.STRING)
    private LibrarianProfileStatus profileStatus = LibrarianProfileStatus.ACTIVE;

    @OneToMany(mappedBy = "librarian", cascade =  CascadeType.ALL)
    private List<LibraryEmployment> employmentHistory = new ArrayList<>();

}