package com.LMS.Library.Management.System.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateEmploymentRequest {
    private String designation;
    private Double salary;
}