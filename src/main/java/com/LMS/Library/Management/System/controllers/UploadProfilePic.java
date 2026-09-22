package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.Security.CustomUserDetails;
import com.LMS.Library.Management.System.dto.ProfileUploadingDto;
import com.LMS.Library.Management.System.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController()
@RequestMapping("/profile")
public class UploadProfilePic {

    @Autowired
    UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@Valid @ModelAttribute ProfileUploadingDto profileUploadingDTO, Authentication authentication){
        CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();


        if(userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login then try again");
        userService.upload(profileUploadingDTO,userId);

        return ResponseEntity.ok("Document Uploaded Successfully");


    }
}
