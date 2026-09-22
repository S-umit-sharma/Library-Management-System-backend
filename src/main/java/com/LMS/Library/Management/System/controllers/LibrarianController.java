package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.Security.CustomUserDetails;
import com.LMS.Library.Management.System.dao.LibraryDao;
import com.LMS.Library.Management.System.dto.LibrarianDetailDto;
import com.LMS.Library.Management.System.dto.LibrarianSearchResponseDto;
import com.LMS.Library.Management.System.dto.LibraryProfileUpdateDto;
import com.LMS.Library.Management.System.services.LibrarianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/librarian")
public class LibrarianController {

    @Autowired
    private LibrarianService service;

    @Autowired
    private LibraryDao libraryDao;

    @PostMapping("/details")
    public ResponseEntity<String> addDetails(
            @RequestBody LibrarianDetailDto librarianDto) {


        service.addDetails(librarianDto);

        return ResponseEntity.ok("Librarian Profile Saved Successfully");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();


        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Session Expired. Please Login Again.");
        }

        return ResponseEntity.ok(service.getLibrarianProfile(userId));
    }

    @PatchMapping("/profile")
    public ResponseEntity<?> profileUpdate(@RequestBody LibraryProfileUpdateDto profileUpdateDto, Authentication authentication){
        CustomUserDetails userDetails =  (CustomUserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getUserId();
        if(userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session Expired");
        service.updateProfile(profileUpdateDto,userId);
        return ResponseEntity.ok(Map.of("message","Profile updated successfuly"));

    }

    @GetMapping("/search")
    public ResponseEntity<Page<LibrarianSearchResponseDto>> searchLibrarians(

            @RequestParam(defaultValue = "") String query,

            @RequestParam(required = false) Double minExp,

            @RequestParam(required = false) Integer maxAge,

            Pageable pageable, Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();

        Integer libraryId = libraryDao.findByUser_UserId(userId).orElseThrow(() -> new RuntimeException("User Not found")).getId();

        return ResponseEntity.ok(
                service.searchAvailableLibrarians(
                        query,
                        minExp,
                        maxAge,
                        pageable, libraryId));
    }

}