package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.dto.OtpDto;
import com.LMS.Library.Management.System.dto.ResendOtpDto;
import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.services.OtpService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/otp")
public class OtpVerificationController {

    private final OtpService otpService;

    public OtpVerificationController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/verify")
    public ResponseEntity<?> otpVerify(@Valid @RequestBody OtpDto otpDto){
         User user = otpService.verifyOtp(otpDto.getEmail(),otpDto.getOtp());

         return ResponseEntity.status(HttpStatus.OK).body(Map.of("message","OTP Verified","userType",user.getUserType()
         ));

    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendOtp(@Valid @RequestBody ResendOtpDto otpDto){
        otpService.resendOtp(otpDto.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message",  "OTP Sent Successfully"));
    }
}
