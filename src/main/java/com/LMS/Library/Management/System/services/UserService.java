package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dao.CityDao;
import com.LMS.Library.Management.System.dto.*;
import com.LMS.Library.Management.System.entities.City;
import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.dao.UserDao;
import com.LMS.Library.Management.System.enums.Status;
import com.LMS.Library.Management.System.utils.OtpGenrator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLOutput;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User  registerUser(RegisterDto registerDto) {


        User u = userDao.findByEmail(registerDto.getEmail()).orElse(null);



        if(u != null) throw new RuntimeException("User already exists");


        User user = new User();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(
                passwordEncoder.encode(registerDto.getPassword())
        );
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
        return userDao.findByEmail(email).get();
    }

    public User saveUser(User user) {
        return userDao.save(user);
    }

    public User findUserById(Integer id) {
        return userDao.findById(id).get();
    }

    public User login(LoginDto loginDto) {
        User user = userDao.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User Not Found"));


        if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))throw new RuntimeException("Invalid Email Or Password");

        return user;

    }

    @Transactional
    public void upload(ProfileUploadingDto profileUploadingDTO, Integer userId) {
        MultipartFile file = profileUploadingDTO.getProfilePic() ;
        if(file==null || file.isEmpty()) throw new RuntimeException("Upload the image");
        User user = userDao.findById(userId).get();
        if(user == null) throw new RuntimeException("User Not Found");
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File directory = new File(UPLOAD_DIR);
        if(!directory.exists()) directory.mkdirs();
        Path path = Paths.get(UPLOAD_DIR, fileName);


        try{
            if(user.getProfilePic() != null){

                Files.deleteIfExists(Paths.get(user.getProfilePic()));

            }
            Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);

            user.setProfilePic(fileName);
            userDao.save(user);
        }catch (IOException ioException){
            throw new RuntimeException("Document Uplaod Failed");
        }

    }

    public User findByUserId(Integer userId) {
        return userDao.findById(userId).orElseThrow(()-> new RuntimeException("User Not Found"));
    }


    public UserProfileDto getUserProfile(Integer userId) {

        User user = userDao.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        UserProfileDto dto = new UserProfileDto();

        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setDob(user.getDob());
        dto.setAddress(user.getAddress());
        dto.setContact(user.getContact());
        dto.setProfilePic(user.getProfilePic());

        dto.setGender(user.getGender());
        dto.setStatus(user.getStatus());
        dto.setUserType(user.getUserType());

        if (user.getCity() != null) {
            dto.setCityId(user.getCity().getId());
            dto.setCityName(user.getCity().getName());
        }

        return dto;
    }


    public UserProfileDto updateUserProfile(
            Integer userId,
            UserProfileUpdateDto dto
    ) {

        User user = userDao.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        // Update basic information
        user.setName(dto.getName());
        user.setDob(dto.getDob());
        user.setAddress(dto.getAddress());
        user.setContact(dto.getContact());
        user.setGender(dto.getGender());

        // Update city
        if (dto.getCityId() != null) {

            City city = cityDao.findById(dto.getCityId())
                    .orElseThrow(() ->
                            new RuntimeException("City not found")
                    );

            user.setCity(city);
        }


        User updatedUser = userDao.save(user);

        return getUserProfile(updatedUser.getUserId());
    }
}
