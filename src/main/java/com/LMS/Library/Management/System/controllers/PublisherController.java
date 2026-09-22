package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.Security.CustomUserDetails;
import com.LMS.Library.Management.System.dao.LibraryDao;
import com.LMS.Library.Management.System.dto.PublisherDto;
import com.LMS.Library.Management.System.dto.PublisherProfileResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.LMS.Library.Management.System.services.PublisherService;

@RestController
@RequestMapping("/publisher")
public class PublisherController {

    @Autowired
    private PublisherService publisherService;

    @Autowired
    private LibraryDao libraryDao;

    @PostMapping("/details")
    public ResponseEntity<String> addDetails(@RequestBody PublisherDto publisherDto){
        publisherService.addDetails(publisherDto);
        return ResponseEntity.status(HttpStatus.OK).body("Publisher Detials Added");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication){
        CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();

        if(userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session Expired Please login Again");
        PublisherProfileResponseDto publisherResponseDto = publisherService.getProfile(userId);
        if(publisherResponseDto == null) ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Complete Your OTP Verification");

        return ResponseEntity.ok(publisherResponseDto);
    }
}
