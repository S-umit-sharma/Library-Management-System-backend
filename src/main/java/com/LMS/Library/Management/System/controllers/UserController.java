package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.dto.LoginDto;
import com.LMS.Library.Management.System.dto.LoginResponseDto;
import com.LMS.Library.Management.System.dto.RegisterDto;
import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.enums.Status;
import com.LMS.Library.Management.System.enums.UserType;
import com.LMS.Library.Management.System.services.LibrarianService;
import com.LMS.Library.Management.System.services.LibraryService;
import com.LMS.Library.Management.System.services.PublisherService;
import com.LMS.Library.Management.System.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    LibraryService libraryService;

    @Autowired
    PublisherService publisherService;

    @Autowired
    LibrarianService librarianService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterDto registerDto, HttpSession httpSession) {
        User saveduser = userService.registerUser(registerDto);
        if (saveduser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
        }
        httpSession.setAttribute("userEmail", saveduser.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body("User Resgistered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpSession session) {
        User user = userService.login(loginDto);
        if (user.getStatus() == Status.PENDING) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                Map.of("code", "OTP_VERIFICATION_REQUIRED", "message", "Please verify your otp"));
        if (user.getStatus() != Status.ACTIVE) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                Map.of(
                        "code", "PROFILE_INCOMPLETE",
                        "message", "Please add the remaining profile details",
                        "userType", user.getUserType()
                )
        );
        LoginResponseDto response = new LoginResponseDto();

        if (user.getUserType() == UserType.PUBLISHER) {

            session.setAttribute("publisherId", publisherService.getProfile(user.getUserId()).getPublisherId());
        } else if (user.getUserType() == UserType.LIBRARY) {

            session.setAttribute("libraryId", libraryService.getLibraryProfile(user.getUserId()).getLibraryId());
        } else if (user.getUserType() == UserType.LIBRARIAN){
            session.setAttribute("librarianId", librarianService.getLibrarianProfile(user.getUserId()).getLibrarianId());
        }

        session.setAttribute("loggedInUser", user.getUserId());
        response.setMessage("Login Successful");
        response.setUserType(user.getUserType());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout   ")
    public ResponseEntity<String> logout(HttpSession session) {
        session.removeAttribute("loggedInUser");
        return ResponseEntity.status(HttpStatus.OK).body("Logged out");
    }


}
