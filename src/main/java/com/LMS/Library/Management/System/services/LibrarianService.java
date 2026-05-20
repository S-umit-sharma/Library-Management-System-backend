package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dao.LibraianDao;
import com.LMS.Library.Management.System.dto.LibrarianDto;
import com.LMS.Library.Management.System.entities.Librarian;
import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.enums.Status;
import com.LMS.Library.Management.System.utils.EmployeeCodeGenrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibrarianService {
    @Autowired
    UserService userService;

    @Autowired
    LibraianDao libraianDao;

    public void addDetails(LibrarianDto librarianDto, Integer id) {
        User user = userService.findUserById(id);
        if(user == null) throw new RuntimeException("User Not Found");
        Librarian librarian = new Librarian();
        librarian.setQualification(librarian.getQualification());
        librarian.setEmployeeCode(EmployeeCodeGenrator.genrateEmpCode(user.getName(),id));
        librarian.setUser(user);
        libraianDao.save(librarian);
        user.setStatus(Status.ACTIVE);
        userService.saveUser(user);

    }
}
