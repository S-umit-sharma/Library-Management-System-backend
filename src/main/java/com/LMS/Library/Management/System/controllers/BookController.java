package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.dto.AddBookDto;
import com.LMS.Library.Management.System.dto.BookReponseDto;
import com.LMS.Library.Management.System.dto.BookUpdateDto;
import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.services.BookService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    BookService bookService;

    @PostMapping("add")
    public ResponseEntity<String> addBook(@ModelAttribute AddBookDto bookDetailDto, HttpSession session){
        Integer userId = (Integer)session.getAttribute("loggedInUser");
        if(userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired login again");
        bookService.addBook(bookDetailDto,userId);
        return ResponseEntity.status(HttpStatus.OK).body("Book Added Successfully");
    }

    @GetMapping("/image/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName){
        try{
        Path path = Paths.get("uploads","books",fileName);
        Resource resource = new UrlResource(path.toUri());
        if(!resource.exists() || !resource.isReadable()){
            return ResponseEntity.notFound().build();
        }
        String contentType = Files.probeContentType(path);
        if(contentType == null){
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
        }catch (IOException e){
            return ResponseEntity.internalServerError().build();
        }


    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Integer id, @ModelAttribute BookUpdateDto dto,HttpSession session){
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

    @GetMapping("all")
    public ResponseEntity<?> getAllBooks(HttpSession session){

        if(session.getAttribute("loggedInUser") == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login again");
        return ResponseEntity.ok(bookService.getAllBooks(0, 10));
    }

    // Global Search
    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(@RequestParam String keyword, @RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size, HttpSession session){

        if( session.getAttribute("loggedInUser") == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login again");

        return ResponseEntity.ok(
                bookService.searchBooks(keyword,page,size)
            );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable int id,HttpSession session){
        Integer userId = (Integer)session.getAttribute("loggedInUser");
        if(userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login again");
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book Deleted Successfully");
    }

}
