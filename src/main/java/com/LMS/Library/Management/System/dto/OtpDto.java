package com.LMS.Library.Management.System.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpDto {

    @NotBlank(message = "Otp is required")
    String otp;

    @NotBlank(message = "Email feild is required")
    @Email
    String email;

}
