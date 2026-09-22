package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.Security.CustomUserDetails;
import com.LMS.Library.Management.System.dao.LibraryDao;
import com.LMS.Library.Management.System.dto.LibraryBookResponseDto;
import com.LMS.Library.Management.System.entities.LibraryBook;
import com.LMS.Library.Management.System.services.LibraryBookService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("library")
public class LibraryBookController {

    @Autowired
    private LibraryBookService bookService;

    @Autowired
    private LibraryDao libraryDao;

    @PostMapping("/addBookToLibrary/{id}")
    public ResponseEntity<?> addBookToLibrary(@PathVariable int id, @RequestParam int quantity, Authentication authentication){
        CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();

        if(userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login again");
        bookService.addBookToLibrary(id,userId,quantity);
        return ResponseEntity.status(HttpStatus.OK).body("Book Added Successfully");
    }

    @DeleteMapping("/removeBookFromLibrary/{id}")
    public ResponseEntity<?> removeBookFromLibrary(@PathVariable int id,@RequestParam int quantity,Authentication authentication){
        CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();

        if(userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login again");
        bookService.removeBookFromLibrary(id,userId,quantity);
        return ResponseEntity.status(HttpStatus.OK).body("Book deleted Successfully");
    }

    @GetMapping("/books/search")
    public ResponseEntity<Page<LibraryBookResponseDto>> searchBooks(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();


        Integer libraryId = libraryDao.findByUser_UserId(userId).orElseThrow(()->new RuntimeException("User Not found")).getId();
        Page<LibraryBookResponseDto> res = bookService.searchBooks(libraryId, keyword, page, size);

        return ResponseEntity.ok(
                res);
    }

}
