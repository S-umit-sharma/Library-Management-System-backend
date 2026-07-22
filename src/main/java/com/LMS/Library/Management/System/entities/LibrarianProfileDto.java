package com.LMS.Library.Management.System.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LibrarianProfileDto {

    @NotBlank(message = "Highest qualification is required.")
    private String highestQualification;

    @NotNull(message = "Experience is required.")
    @PositiveOrZero(message = "Experience cannot be negative.")
    private Double totalExperienceYears;

    @NotBlank(message = "Specialization is required.")
    private String specialization;

    private String certifications;

    @NotBlank(message = "Preferred designation is required.")
    private String preferredDesignation;

    private String bio;
}