package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.dto.OtpDto;
import com.LMS.Library.Management.System.dto.ResendOtpDto;
import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.services.OtpService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otp")
public class OtpVerficationController {

    @Autowired
    OtpService otpService;

    @PostMapping("/verify")
    public ResponseEntity<String> otpVerify(@Valid @RequestBody OtpDto otpDto, HttpSession httpSession){
         String email = (String)httpSession.getAttribute("userEmail");
         if(email == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session Expired");

         User user = otpService.verifyOtp(email,otpDto.getOtp());
         httpSession.removeAttribute("userEmail");
         httpSession.setAttribute("loggedIn user",user.getUserId());
         return ResponseEntity.status(HttpStatus.OK).body("OTP Verified");

    }

    @PostMapping("/resend")
    public ResponseEntity<String> resendOtp(@Valid @RequestBody ResendOtpDto otpDto,HttpSession session){
        otpService.resendOtp(otpDto.getEmail());
        session.setAttribute("userEmail",otpDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body("OTP Sent Successfully");
    }
}
