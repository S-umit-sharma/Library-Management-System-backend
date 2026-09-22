package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.Security.CustomUserDetails;
import com.LMS.Library.Management.System.dto.UserDocumentDto;
import com.LMS.Library.Management.System.services.DocumentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/document")
public class UploadDocumentController {

    @Autowired
    private DocumentService service;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(UserDocumentDto userDocumentDTO, Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();

        if(userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login then try again");
        service.upload(userDocumentDTO,userId);

        return ResponseEntity.ok("Document Uploaded Successfully");
    }
}