package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dto.LibraryDto;
import com.LMS.Library.Management.System.entities.Library;
import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.dao.LibrayDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibraryService {
    @Autowired
    private LibrayDao librayDao;

    @Autowired
    private UserService userService;

    public Library addDetails(LibraryDto libraryDto,Integer id) {
        User user = userService.findUserById(id);
        Library library = new Library();
        library.setUser(user);
        library.setDetails(libraryDto.getDetails());
        library.setWebsite(libraryDto.getWebsite());
        library.setOpeningTime(libraryDto.getOpeningTime());
        library.setClosingTime(libraryDto.getClosingTime());
        library.setBookIssueDays(libraryDto.getBookIssueDays());
        library.setLateFine(libraryDto.getLateFine());
        library.setDepositAmount(libraryDto.getDepositAmount());

        return librayDao.save(library);
    }
}
