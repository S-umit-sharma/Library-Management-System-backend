package com.LMS.Library.Management.System.dto;

import com.LMS.Library.Management.System.enums.LibrarianProfileStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibrarianSearchResponseDto {

    private Integer librarianId;

    // User Information
    private String name;
    private String email;
    private String contact;
    private String profilePic;
    private Integer age;
    private String location;

    // Professional Information
    private String highestQualification;
    private Double totalExperienceYears;
    private String specialization;
    private String preferredDesignation;
    private LibrarianProfileStatus profileStatus;

    // Hiring Status
    private Boolean availableForHire;
    private boolean hired;
}