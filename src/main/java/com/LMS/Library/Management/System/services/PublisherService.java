package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dao.PublisherDao;
import com.LMS.Library.Management.System.dto.PublisherDto;
import com.LMS.Library.Management.System.dto.PublisherProfileResponseDto;
import com.LMS.Library.Management.System.entities.Publisher;
import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.enums.Status;
import com.LMS.Library.Management.System.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {
    @Autowired
    private PublisherDao publisherDao;

    @Autowired
    private UserService userService;

    public Publisher addDetails(PublisherDto publisherDto, Integer id) {

        User user = userService.findUserById(id);

        Publisher publisher = new Publisher();
        publisher.setUser(user);
        publisher.setCompanyName(publisherDto.getCompanyName());
        publisher.setGstNumber(publisherDto.getGstNumber());
        publisher.setWebsite(publisherDto.getWebsite());
        publisher.setDescription(publisherDto.getDescription());
        publisherDao.save(publisher);
        user.setStatus(Status.ACTIVE);
        userService.saveUser(user);
        return publisher;
    }

    public PublisherProfileResponseDto getProfile(Integer userId) {

        User user = userService.findUserById(userId);
        if (user == null) throw new RuntimeException("User not Found");
        if(user.getUserType() != UserType.PUBLISHER) throw new RuntimeException("Unauthorize access");
        System.out.println(user);
        Publisher publisher = publisherDao.findByUser(user);
        System.out.println(publisher);
        if (publisher == null) throw new RuntimeException("Publisher profile not found");

        PublisherProfileResponseDto dto = new PublisherProfileResponseDto();

        dto.setPublisherId(publisher.getPublisherId());
        dto.setCompanyName(publisher.getCompanyName());
        dto.setWebsite(publisher.getWebsite());
        dto.setGstNumber(publisher.getGstNumber());
        dto.setDescription(publisher.getDescription());

        dto.setUserId(publisher.getUser().getUserId());
        dto.setName(publisher.getUser().getName());
        dto.setEmail(publisher.getUser().getEmail());
        dto.setContact(publisher.getUser().getContact());
        dto.setAddress(publisher.getUser().getAddress());
        dto.setProfilePic(publisher.getUser().getProfilePic());

        if (publisher.getUser().getCity() != null) {
            dto.setCity(publisher.getUser().getCity().getName());
        }

        return dto;

    }


}

