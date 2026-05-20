package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.enums.Status;
import com.LMS.Library.Management.System.enums.UserType;
import com.LMS.Library.Management.System.utils.OtpGenrator;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    public User verifyOtp(String email, String otp) {

        User user = userService.findUserByEmail(email);

        if (user == null) throw new RuntimeException("User Not Found");

        if (!user.getVerificationCode().equals(otp)) {
            throw new RuntimeException("Invalid Otp");

        }
        if (user.getUserType() == UserType.STUDENT) user.setStatus(Status.ACTIVE);
        else if(user.getUserType() == UserType.LIBRARIAN) user.setStatus(Status.VERIFIED);
        else user.setStatus(Status.VERIFIED);
        user.setVerificationCode(null);
        userService.saveUser(user);


        return user;
    }

    public void resendOtp(@NotBlank(message = "Email is required") String email) {
        User user = userService.findUserByEmail(email);
        if (user == null) throw new RuntimeException("User Not Found");
        if (user.getStatus() == Status.ACTIVE) throw new RuntimeException("User already verfied");
        String otp = OtpGenrator.genrateOtp();
        user.setVerificationCode(otp);
        userService.saveUser(user);
        emailService.sendOtp(email, otp);
    }


}
