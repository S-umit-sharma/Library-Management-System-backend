package com.LMS.Library.Management.System.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class LibraryDto {
    @NotBlank(message = "Enter the Description Of Your Library")
    private String details;
    @NotBlank(message = "Enter the Website Url")
    private String website;
    @NotBlank(message = "Enter the Library Opning Time")
    private LocalTime openingTime;
    @NotBlank(message = "Enter the Library Clsoing Time")
    private LocalTime closingTime;
    @NotBlank(message = "Enter the Book Issued Days")
    private Integer bookIssueDays;
    @NotBlank(message = "Enter the Late Fine Amount")
    private Integer lateFine;
    @NotBlank(message = "Enter the Desposite Amount")
    private Integer depositAmount;
}
