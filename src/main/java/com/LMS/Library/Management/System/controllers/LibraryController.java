package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.Security.CustomUserDetails;
import com.LMS.Library.Management.System.Security.CustomUserDetailsService;
import com.LMS.Library.Management.System.dto.LibraryDashboardDto;
import com.LMS.Library.Management.System.dto.LibraryDto;
import com.LMS.Library.Management.System.dto.LibraryResponseDto;
import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.enums.UserType;
import com.LMS.Library.Management.System.services.LibraryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/library")
public class LibraryController {

    @Autowired
    LibraryService libraryService;

    @PostMapping("/details")
    public ResponseEntity<String> addDetails(@RequestBody LibraryDto libraryDto){
        libraryService.addDetails(libraryDto);
        return ResponseEntity.status(HttpStatus.OK).body("Library details saved successfully!");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getLibraryProfile(Authentication authentication){
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();

        if(userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session Expired Please login Again");
        LibraryResponseDto libraryResponseDto = libraryService.getLibraryProfile(userId);
        if(libraryResponseDto == null) ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Complete Your OTP Verification");


        return ResponseEntity.ok(libraryResponseDto);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<LibraryDashboardDto> getDashboard(Authentication authentication){

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();


        return ResponseEntity.ok(
                libraryService.getDashboard(userId)
        );
    }




}
