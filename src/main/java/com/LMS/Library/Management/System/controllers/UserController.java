package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.Security.JwtUtil;
import com.LMS.Library.Management.System.dto.*;
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
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> registerUser(@Valid @RequestBody RegisterDto registerDto) {

        User saveduser = userService.registerUser(registerDto);
        if (saveduser == null) {
            throw new RuntimeException("Registration Failed");
        }

        return ResponseEntity.ok(new RegisterResponseDto(
                "OTP sent successfullyyy",
                saveduser.getEmail()
        ));
    }


@PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {

    User user = userService.login(loginDto);
    if (user.getStatus() == Status.PENDING) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            Map.of("code", "OTP_VERIFICATION_REQUIRED", "message", "Please verify your otp"));
    if (user.getStatus() == Status.VERIFIED) return ResponseEntity.status(HttpStatus.OK).body(
            Map.of(
                    "code", "PROFILE_INCOMPLETE",
                    "message", "Please add the remaining profile details",
                    "userType", user.getUserType()
            )
    );


    String token  = jwtUtil.genrateToken(user.getUserId(), user.getEmail(), user.getUserType().name());

    LoginResponseDto response = new LoginResponseDto();


    response.setMessage("Login Successful");
    response.setUserType(user.getUserType());
    response.setToken(token);
    response.setUserId(user.getUserId());
    return ResponseEntity.ok(response);
}


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.removeAttribute("loggedInUser");
        return ResponseEntity.status(HttpStatus.OK).body("Logged out");
    }

    @GetMapping("/profile_img/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName){
        System.out.println("Inside the getImage ---------------------------");
        try{
            Path path = Paths.get("uploads","profile_pics",fileName);
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


}
