package com.LMS.Library.Management.System.dto;

import com.LMS.Library.Management.System.enums.UserType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {

    private String message;
    private UserType userType;
}
