package com.LMS.Library.Management.System.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublisherDto {
    @NotBlank(message = "Enter the company name")
    private String companyName;

    @NotBlank(message = "Enter the website")
    private String website;

    @NotBlank(message = "Enter the GST number")
    private String gstNumber;

    @NotBlank(message = "Enter the description")
    private String description;
}
