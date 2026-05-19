package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.services.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/membership")
public class MembershipController {

    @Autowired
    MembershipService service;
//
//    @PostMapping("/details")
//    public ResponseEntity<String> addDetails(@RequestBody LibraryDto libraryDto, HttpSession session){
//        Integer id = (Integer) session.getAttribute("loggedInUser");
//        if(id == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Required");
//        service.addDetails(libraryDto, id);
//        return ResponseEntity.status(HttpStatus.OK).body("Student Detials Added");
//    }
}
