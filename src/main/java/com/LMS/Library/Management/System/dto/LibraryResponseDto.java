package com.LMS.Library.Management.System.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class LibraryResponseDto {

    private Integer libraryId;

    private String address;
    private String contact;
    private LocalDate registrationDate;
    private String email;
    private String name;
    private String profilePic;
    private String cityName;
    private String stateName;

    private Integer bookIssueDays;
    private LocalTime closingTime;
    private Integer depositAmount;
    private String details;
    private Integer lateFine;
    private LocalTime openingTime;
    private String website;


}