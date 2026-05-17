package com.LMS.Library.Management.System.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOtp(String toEmail,String otp){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject("Email Verification OTP");
        simpleMailMessage.setText("Your OTP for email verfication is:" + otp);
        javaMailSender.send(simpleMailMessage);
    }
}
