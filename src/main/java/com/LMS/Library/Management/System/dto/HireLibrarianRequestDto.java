package com.LMS.Library.Management.System.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class HireLibrarianRequestDto {

    @NotBlank(message = "Designation is required")
    private String designation;

    private Double salary;

    private LocalDate joinedOn; // defaults to today if null
}

