package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.dto.LibraryDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/library")
public class LibraryController {


    @RequestMapping("/details")
    public ResponseEntity<String> addDetails(@RequestBody LibraryDto libraryDto){
        return ResponseEntity.status(HttpStatus.OK).body("Here Till the Resend and OTP verifcation is complete");
    }
}
