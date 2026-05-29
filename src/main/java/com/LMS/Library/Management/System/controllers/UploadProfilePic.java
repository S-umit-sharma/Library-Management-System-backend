package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.dto.ProfileUploadingDto;
import com.LMS.Library.Management.System.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController()
@RequestMapping("/profile")
public class UploadProfilePic {

    @Autowired
    UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(ProfileUploadingDto profileUploadingDTO, HttpSession session){
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        if(userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login then try again");
        userService.upload(profileUploadingDTO,userId);

        return ResponseEntity.ok("Document Uploaded Successfully");


    }
}
