package com.LMS.Library.Management.System.dto;

import com.LMS.Library.Management.System.enums.Gender;
import com.LMS.Library.Management.System.enums.LibrarianProfileStatus;
import com.LMS.Library.Management.System.enums.Status;
import com.LMS.Library.Management.System.enums.UserType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LibrarianProfileResponseDto {

    // User details
    private Integer userId;
    private String name;
    private String email;
    private String contact;
    private LocalDate dob;
    private String address;
    private String profilePic;
    private Gender gender;
    private UserType userType;
    private Status status;

    // Librarian details
    private Integer librarianId;
    private String highestQualification;
    private Double totalExperienceYears;
    private String specialization;
    private String certifications;
    private String preferredDesignation;
    private String bio;
    private Boolean availableForHire;
    private LibrarianProfileStatus profileStatus;

    // Current employment/library
//    private LibraryEmploymentResponseDto currentEmployment;
}
