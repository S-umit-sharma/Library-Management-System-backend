package com.LMS.Library.Management.System.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResendOtpDto {

    @NotBlank(message = "Email is required")
    String email;
}
