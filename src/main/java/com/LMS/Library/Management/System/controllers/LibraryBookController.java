package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.services.LibraryBookService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("book")
public class LibraryBookController {

    @Autowired
    LibraryBookService bookService;

    @PostMapping("/addBookToLibrary/{id}")
    public ResponseEntity<?> addBookToLibrary(@PathVariable int id, @RequestParam int quantity, HttpSession session){
        Integer loggedInUser =
                (Integer) session.getAttribute("loggedInUser");

        if(loggedInUser == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login again");
        bookService.addBookToLibrary(id,loggedInUser,quantity);
        return ResponseEntity.status(HttpStatus.OK).body("Book Added Successfully");
    }
}
