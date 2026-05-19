package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.dto.LibraryDto;
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
        return ResponseEntity.status(HttpStatus.OK).body("Library Detials Added");
    }



}
