package com.LMS.Library.Management.System.dto;
import com.LMS.Library.Management.System.enums.LibrarianStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LibrarianProfileDto {

    private Integer librarianId;
    // User Data
    private String name;

    private String email;

    private String contact;

    // Librarian Data
    private String employeeCode;

    private String qualification;

    private String designation;

    private Integer experienceYears;

    private LocalDate joinedOn;

    private LibrarianStatus status;

    // Library Data
    private Integer libraryId;

    private String libraryName;

    private String website;
}