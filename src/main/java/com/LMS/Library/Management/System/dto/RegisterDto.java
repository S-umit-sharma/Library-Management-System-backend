package com.LMS.Library.Management.System.dto;

import com.LMS.Library.Management.System.enums.Gender;

import com.LMS.Library.Management.System.enums.UserType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterDto {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 6, message = "Password must be atleast 6 characters")
    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Date of birth is required")
    private LocalDate dob;

    @NotNull(message = "City is required")
    private Long cityId;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Contact is required")
    private String contact;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Please Select the user type")
    private UserType userType;

    // getters setters
}