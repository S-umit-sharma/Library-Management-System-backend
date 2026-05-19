package com.LMS.Library.Management.System.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    @NotBlank(message = "Email is Required")
    @Email(message = "Invalid email")
    String email;
    @NotBlank(message = "Password is Required")
    String password;
}
