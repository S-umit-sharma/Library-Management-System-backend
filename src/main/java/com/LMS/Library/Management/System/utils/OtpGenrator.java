package com.LMS.Library.Management.System.utils;

import java.util.Random;

public class OtpGenrator {

    public static String genrateOtp(){
        Random random = new Random();
        String otp = String.valueOf(100000 + random.nextInt(900000));
        return otp;
    }
}
