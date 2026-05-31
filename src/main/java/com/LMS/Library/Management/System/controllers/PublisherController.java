package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.dto.PublisherDto;
import com.LMS.Library.Management.System.dto.PublisherProfileResponseDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.LMS.Library.Management.System.services.PublisherService;

@RestController
@RequestMapping("/publisher")
public class PublisherController {

    @Autowired
    private PublisherService publisherService;

    @PostMapping("/details")
    public ResponseEntity<String> addDetails(@RequestBody PublisherDto publisherDto, HttpSession session){
        Integer id = (Integer) session.getAttribute("loggedInUser");
        if(id == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Required");
        publisherService.addDetails(publisherDto, id);
        return ResponseEntity.status(HttpStatus.OK).body("Publisher Detials Added");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpSession httpSession){
        Integer userId = (Integer)httpSession.getAttribute("loggedInUser");

        if(userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session Expired Please login Again");
        PublisherProfileResponseDto publisherResponseDto = publisherService.getProfile(userId);
        if(publisherResponseDto == null) ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Complete Your OTP Verification");

        return ResponseEntity.ok(publisherResponseDto);
    }
}
