package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dto.LibraryDto;
import com.LMS.Library.Management.System.dto.LibraryResponseDto;
import com.LMS.Library.Management.System.entities.Book;
import com.LMS.Library.Management.System.entities.Library;
import com.LMS.Library.Management.System.entities.LibraryBook;
import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.dao.LibrayDao;
import com.LMS.Library.Management.System.enums.Status;
import com.LMS.Library.Management.System.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibraryService {
    @Autowired
    private LibrayDao librayDao;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    public Library addDetails(LibraryDto libraryDto, Integer id) {
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
        user.setStatus(Status.ACTIVE);
        userService.saveUser(user);
        return librayDao.save(library);
    }

    public LibraryResponseDto getLibraryProfile(Integer userId) {
        User user = userService.findUserById(userId);

        if (user == null) throw new RuntimeException("User Not Found");

        if (user.getUserType() != UserType.LIBRARY) throw new RuntimeException("User Not Verified");
        Library libary = librayDao.getReferenceById(user.getUserId());
        LibraryResponseDto libraryResponseDto = new LibraryResponseDto();
        libraryResponseDto.setLibraryId(libary.getId());
        libraryResponseDto.setLibraryName(user.getName());
        libraryResponseDto.setEmail(user.getEmail());
        libraryResponseDto.setWebsite(libary.getWebsite());
        libraryResponseDto.setDetails(libary.getDetails());
        libraryResponseDto.setOpeningTime(libary.getOpeningTime());
        libraryResponseDto.setClosingTime(libary.getClosingTime());
        libraryResponseDto.setBookIssueDays(libary.getBookIssueDays());
        libraryResponseDto.setLateFine(libraryResponseDto.getLateFine());
        libraryResponseDto.setDepositAmount(libary.getDepositAmount());


        return libraryResponseDto;
    }


}
