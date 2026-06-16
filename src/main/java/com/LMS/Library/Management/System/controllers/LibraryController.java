package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.dto.LibraryDto;
import com.LMS.Library.Management.System.dto.LibraryResponseDto;
import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.enums.UserType;
import com.LMS.Library.Management.System.services.LibraryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/library")
public class LibraryController {

    @Autowired
    LibraryService libraryService;

    @PostMapping("/details")
    public ResponseEntity<String> addDetails(@RequestBody LibraryDto libraryDto, HttpSession session){
        Integer id = (Integer) session.getAttribute("loggedInUser");
        if(id == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Required");
        libraryService.addDetails(libraryDto, id);
        return ResponseEntity.status(HttpStatus.OK).body("Library details saved successfully!");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getLibraryProfile(HttpSession httpSession){
        Integer userId = (Integer)httpSession.getAttribute("loggedInUser");

        if(userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session Expired Please login Again");
        LibraryResponseDto libraryResponseDto = libraryService.getLibraryProfile(userId);
        if(libraryResponseDto == null) ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Complete Your OTP Verification");

        return ResponseEntity.ok(libraryResponseDto);
    }




}
