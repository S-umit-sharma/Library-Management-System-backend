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

import java.util.Map;

@RestController
@RequestMapping("/otp")
public class OtpVerficationController {

    @Autowired
    OtpService otpService;

    @PostMapping("/verify")
    public ResponseEntity<?> otpVerify(@Valid @RequestBody OtpDto otpDto, HttpSession httpSession){
         String email = (String)httpSession.getAttribute("userEmail");
         if(email == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("code","SESSION_EXPIRED","message","Your verification session has expired. Please resend the OTP to continue."));

         User user = otpService.verifyOtp(email,otpDto.getOtp());
         httpSession.removeAttribute("userEmail");
         httpSession.setAttribute("loggedInUser",user.getUserId());
         return ResponseEntity.status(HttpStatus.OK).body(Map.of("message","OTP Verified","userType",user.getUserType()
         ));

    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendOtp(@Valid @RequestBody ResendOtpDto otpDto,HttpSession session){
        otpService.resendOtp(otpDto.getEmail());
        session.setAttribute("userEmail",otpDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body("OTP Sent Successfully");
    }
}
