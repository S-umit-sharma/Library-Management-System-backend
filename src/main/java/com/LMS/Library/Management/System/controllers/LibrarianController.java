package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.dto.LibrarianDto;
import com.LMS.Library.Management.System.dto.LibrarianProfileDto;
import com.LMS.Library.Management.System.services.LibrarianService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpClient;

@RestController
@RequestMapping("/librarian")
public class LibrarianController {

    @Autowired
    LibrarianService service;

    @PostMapping("/details")
    public ResponseEntity<String> addDetails(@RequestBody LibrarianDto librarianDto, HttpSession httpSession){
        Integer id = (Integer)httpSession.getAttribute("loggedInUser");
        if(id == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Required");
        service.addDetails(librarianDto,id);
        return ResponseEntity.ok("Libraian Details Added");

    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(HttpSession session){
        Integer userId = (Integer)session.getAttribute("loggedInUser");
        if(userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session Expired Please login Again");
        LibrarianProfileDto librarianResponseDto = service.getLibrarianProfile(userId);
        return ResponseEntity.ok(librarianResponseDto);
    }
}
