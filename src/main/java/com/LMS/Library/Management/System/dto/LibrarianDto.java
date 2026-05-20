package com.LMS.Library.Management.System.dto;

import com.LMS.Library.Management.System.entities.Library;
import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class LibrarianDto {
//
//
//    private String employeeCode;
//
//    private String designation;
//
//    private LocalDate joinedOn;
//
//    private LocalDate leftOn;
//
//    private Double salary;
//
//    private Integer experienceYears;

    @NotBlank(message = "Enter the Qualification")
    private String qualification;


}
