package com.LMS.Library.Management.System.dto;

import com.LMS.Library.Management.System.enums.EmploymentStatus;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LibraryEmploymentResponseDto {

    private Integer employmentId;

    // Librarian info
    private Integer librarianId;
    private String librarianName;
    private String librarianEmail;
    private String librarianContact;
    private String profilePic;
    private String highestQualification;
    private Double totalExperienceYears;
    private String specialization;
    private String preferredDesignation;

    // Employment info
    private String employeeCode;
    private String designation;
    private Double salary;
    private LocalDate joinedOn;
    private LocalDate leftOn;
    private EmploymentStatus employmentStatus;

    // Library info
    private Integer libraryId;
    private String libraryName;
}