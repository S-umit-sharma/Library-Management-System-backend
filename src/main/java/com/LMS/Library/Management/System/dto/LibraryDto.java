package com.LMS.Library.Management.System.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class LibraryDto {
    @NotBlank(message = "Enter the Description")
    private String details;
    private String website;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Integer bookIssueDays;
    private Integer lateFine;
    private Integer depositAmount;
}
