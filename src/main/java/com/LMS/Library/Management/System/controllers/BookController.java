package com.LMS.Library.Management.System.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
public class BookController {

    @RequestMapping("add")
    public ResponseEntity<String> add(@RequestBody BookDetailDto){

    }

}
