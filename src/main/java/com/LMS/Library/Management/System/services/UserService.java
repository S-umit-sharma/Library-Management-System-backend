package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dao.CityDao;
import com.LMS.Library.Management.System.dto.LoginDto;
import com.LMS.Library.Management.System.dto.RegisterDto;
import com.LMS.Library.Management.System.entities.City;
import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.dao.UserDao;
import com.LMS.Library.Management.System.enums.Gender;
import com.LMS.Library.Management.System.enums.Status;
import com.LMS.Library.Management.System.utils.OtpGenrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    CityDao cityDao;

    @Autowired
    EmailService emailService;

    public User registerUser(RegisterDto registerDto) {
        User user = new User();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(registerDto.getPassword());
        user.setDob(registerDto.getDob());

        City city = cityDao.findById(registerDto.getCityId()).orElseThrow(() -> new RuntimeException("City Not Found"));
        user.setCity(city);
        user.setAddress(registerDto.getAddress());
        user.setContact(registerDto.getContact());
        user.setGender(registerDto.getGender());
        user.setUserType(registerDto.getUserType());

        user.setStatus(Status.PENDING);

        String otp = OtpGenrator.genrateOtp();
        user.setVerificationCode(otp);
//        emailService.sendOtp(user.getEmail(), otp);
        return userDao.save(user);
    }



    public User findUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public User saveUser(User user) {
        return userDao.save(user);
    }

    public User findUserById(Integer id) {
        return userDao.findById(id).get();
    }

    public User login(LoginDto loginDto) {
        User user = userDao.findByEmail(loginDto.getEmail());
        if(user == null) throw new RuntimeException("User Not Found");

        if(!user.getPassword().equals(loginDto.getPassword()))throw new RuntimeException("Please Enter Correct Password");

        return user;

    }
}
