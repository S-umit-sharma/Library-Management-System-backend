package com.LMS.Library.Management.System.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Enter the Library Opning Time")
    private LocalTime openingTime;
    @NotNull(message = "Enter the Library Clsoing Time")
    private LocalTime closingTime;
    @NotNull(message = "Enter the Book Issued Days")
    private Integer bookIssueDays;
    @NotNull(message = "Enter the Late Fine Amount")
    private Integer lateFine;
    @NotNull(message = "Enter the Desposite Amount")
    private Integer depositAmount;
}
