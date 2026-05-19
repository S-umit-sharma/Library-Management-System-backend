package com.LMS.Library.Management.System.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class LibraryResponseDto {

    private Integer libraryId;

    private String libraryName;

    private String email;

    private String website;

    private String details;

    private LocalTime openingTime;

    private LocalTime closingTime;

    private Integer bookIssueDays;

    private Integer lateFine;

    private Integer depositAmount;
}