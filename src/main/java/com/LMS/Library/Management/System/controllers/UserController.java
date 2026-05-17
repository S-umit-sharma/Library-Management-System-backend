package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.dto.RegisterDto;
import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.enums.Status;
import com.LMS.Library.Management.System.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterDto registerDto, HttpSession httpSession) {
        User saveduser = userService.registerUser(registerDto);
        if (saveduser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
        }
        httpSession.setAttribute("userEmail", saveduser.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body("User Resgistered");
    }

}
