package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.dto.UserDocumentDTO;
import com.LMS.Library.Management.System.enums.DocumentType;
import com.LMS.Library.Management.System.services.DocumentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/document")
public class UploadDocumentController {

    @Autowired
    private DocumentService service;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(UserDocumentDTO userDocumentDTO, HttpSession session) {

        Integer userId = (Integer) session.getAttribute("loggedInUser");
        if(userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login then try again");
        service.upload(userDocumentDTO,userId);

        return ResponseEntity.ok("Document Uploaded Successfully");
    }
}