package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.dto.LibrarianDetailDto;
import com.LMS.Library.Management.System.dto.LibrarianSearchResponseDto;
import com.LMS.Library.Management.System.enums.LibrarianProfileStatus;
import com.LMS.Library.Management.System.services.LibrarianService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/librarian")
public class LibrarianController {

    @Autowired
    private LibrarianService service;

    @PostMapping("/details")
    public ResponseEntity<String> addDetails(
            @RequestBody LibrarianDetailDto librarianDto,
            HttpSession session) {


        Integer userId = (Integer) session.getAttribute("loggedInUser");


        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Login Required");
        }

        service.addDetails(librarianDto, userId);

        return ResponseEntity.ok("Librarian Profile Saved Successfully");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(HttpSession session) {

        Integer userId = (Integer) session.getAttribute("loggedInUser");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Session Expired. Please Login Again.");
        }

        return ResponseEntity.ok(service.getLibrarianProfile(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<LibrarianSearchResponseDto>> searchLibrarians(

            @RequestParam(defaultValue = "") String query,

            @RequestParam(required = false) Double minExp,

            @RequestParam(required = false) Integer maxAge,

            Pageable pageable) {

        return ResponseEntity.ok(
                service.searchAvailableLibrarians(
                        query,
                        minExp,
                        maxAge,
                        pageable));
    }

}