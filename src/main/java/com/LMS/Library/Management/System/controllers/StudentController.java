package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.Security.CustomUserDetails;
import com.LMS.Library.Management.System.dto.UserProfileDto;
import com.LMS.Library.Management.System.dto.UserProfileUpdateDto;
import com.LMS.Library.Management.System.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile(
            Authentication authentication
            ) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();
        if (userId == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        UserProfileDto profile =
                userService.getUserProfile(userId);

        return ResponseEntity.ok(profile);
    }


    @PatchMapping(
            value = "/profile",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<UserProfileDto> updateProfile(
            @ModelAttribute UserProfileUpdateDto dto,
            Authentication authentication
    ) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();

        if (userId == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        UserProfileDto updatedProfile =
                userService.updateUserProfile(userId, dto);

        return ResponseEntity.ok(updatedProfile);
    }



}
