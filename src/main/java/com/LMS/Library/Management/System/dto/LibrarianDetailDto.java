package com.LMS.Library.Management.System.dto;

import com.LMS.Library.Management.System.enums.LibrarianProfileStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LibrarianDetailDto {



    // Librarian Profile Details
    private String highestQualification;

    private Double totalExperienceYears;

    private String specialization;

    private String certifications;

    private String preferredDesignation;

    private String bio;


}