package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dao.CityDao;
import com.LMS.Library.Management.System.dto.LoginDto;
import com.LMS.Library.Management.System.dto.ProfileUploadingDto;
import com.LMS.Library.Management.System.dto.RegisterDto;
import com.LMS.Library.Management.System.entities.City;
import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.dao.UserDao;
import com.LMS.Library.Management.System.enums.Status;
import com.LMS.Library.Management.System.utils.OtpGenrator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CityDao cityDao;

    @Autowired
    private EmailService emailService;

    private String UPLOAD_DIR = "uploads/profile_pics";

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

    @Transactional
    public void upload(ProfileUploadingDto profileUploadingDTO, Integer userId) {
        MultipartFile file = profileUploadingDTO.getMultipartFile();
        if(file==null || file.isEmpty()) throw new RuntimeException("Upload the image");
        User user = userDao.findById(userId).get();
        if(user == null) throw new RuntimeException("User Not Found");

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File directory = new File(UPLOAD_DIR);
        if(!directory.exists()) directory.mkdirs();
        Path path = Paths.get(UPLOAD_DIR, fileName);
        try{
            Files.deleteIfExists(Paths.get(user.getProfilePic()));
            Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
            user.setProfilePic(path.toString());
            userDao.save(user);
        }catch (IOException ioException){
            throw new RuntimeException("Document Uplaod Failed");
        }

    }
}
