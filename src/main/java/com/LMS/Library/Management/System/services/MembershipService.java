package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dao.MembershipDao;
import com.LMS.Library.Management.System.dto.LibraryDto;
import com.LMS.Library.Management.System.entities.Membership;
import com.LMS.Library.Management.System.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembershipService {

    @Autowired
    UserService userService;

    @Autowired
    MembershipDao membershipDao;

//    public Membership addDetails(LibraryDto libraryDto, Integer id) {
//        User user = userService.findUserById(id);
//        if(user == null) throw new RuntimeException("User not Found");
//        Membership student = new Membership();
////        student
//
//
//        return membershipDao.save(student);
//    }
}
