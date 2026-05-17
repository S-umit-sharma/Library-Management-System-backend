package com.LMS.Library.Management.System.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpDto {

    @NotBlank(message = "Otp is required")
    String otp;

}
