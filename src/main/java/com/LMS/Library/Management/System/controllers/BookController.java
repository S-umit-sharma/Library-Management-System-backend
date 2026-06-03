package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.dto.AddBookDto;
import com.LMS.Library.Management.System.dto.BookReponseDto;
import com.LMS.Library.Management.System.dto.BookUpdateDto;
import com.LMS.Library.Management.System.services.BookService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    BookService bookService;

    @RequestMapping("add")
    public ResponseEntity<String> addBook(@ModelAttribute AddBookDto bookDetailDto, HttpSession session){
        Integer userId = (Integer)session.getAttribute("loggedInUser");
        if(userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired login again");
        bookService.addBook(bookDetailDto,userId);
        return ResponseEntity.status(HttpStatus.OK).body("Book Added Successfully");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Integer id, @RequestBody BookUpdateDto dto,HttpSession session){
        Integer userId = (Integer)session.getAttribute("loggedInUser");
        if(userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Again");
        bookService.updateBook(id,dto);

        return ResponseEntity.status(HttpStatus.OK).body("Book updated successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable Integer id, HttpSession session){
        Integer userId = (Integer)session.getAttribute("loggedInUser");
        if(userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Again");
        BookReponseDto  bookReponseDto = bookService.getBook(id);

        return ResponseEntity.status(HttpStatus.OK).body(bookReponseDto);

    }

    @GetMapping("/publisher/{id}")
    public ResponseEntity<Page<BookReponseDto>> getAllBooksByPublisherId(@PathVariable Integer id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue ="10") int size){
        return ResponseEntity.ok(bookService.getAllBooksByPublisherId(id,page,size));
    }
}
