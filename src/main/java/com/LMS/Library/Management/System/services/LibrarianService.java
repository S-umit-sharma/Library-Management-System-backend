package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dao.LibraianDao;
import com.LMS.Library.Management.System.dao.LibraryDao;
import com.LMS.Library.Management.System.dto.LibrarianDetailDto;
import com.LMS.Library.Management.System.dto.LibrarianProfileDto;
import com.LMS.Library.Management.System.dto.LibrarianSearchResponseDto;
import com.LMS.Library.Management.System.entities.Librarian;
import com.LMS.Library.Management.System.entities.Library;
import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.enums.LibrarianProfileStatus;
import com.LMS.Library.Management.System.enums.Status;
import com.LMS.Library.Management.System.enums.UserType;
import com.LMS.Library.Management.System.utils.EmployeeCodeGenrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;

@Service
public class LibrarianService {
    @Autowired
    UserService userService;

    @Autowired
    LibraianDao libraianDao;

    @Autowired
    LibraryDao libraryDao;


    public void addDetails(LibrarianDetailDto librarianDto, Integer id) {
        User user = userService.findUserById(id);
        if (user == null) throw new RuntimeException("User Not Found");
        if (user.getStatus() == Status.PENDING) throw new RuntimeException("Verify OTP then try again");
        Librarian librarian = new Librarian();
        librarian.setHighestQualification(librarianDto.getHighestQualification());
        librarian.setTotalExperienceYears(librarianDto.getTotalExperienceYears());
        librarian.setSpecialization(librarianDto.getSpecialization());
        librarian.setCertifications(librarian.getCertifications());
        librarian.setPreferredDesignation(librarian.getPreferredDesignation());
        librarian.setBio(librarianDto.getBio());
        librarian.setUser(user);
        libraianDao.save(librarian);
        user.setStatus(Status.ACTIVE);
        userService.saveUser(user);

    }

    public LibrarianProfileDto getLibrarianProfile(Integer userId) {
        User user = userService.findUserById(userId);
        if (user == null) throw new RuntimeException("Invalid logged-in user");
        Librarian librarian = libraianDao.findByUser(user).orElseThrow(()->new RuntimeException("User Not Found"));
        if (user.getUserType() != UserType.LIBRARIAN) throw new RuntimeException("User not verified");
        if (librarian == null) throw new RuntimeException("librarian profile not completed yet");
        LibrarianProfileDto librarianProfileDto = new LibrarianProfileDto();


        // Librarian Details
//        librarianProfileDto.setEmployeeCode(librarian.getEmployeeCode());
//        librarianProfileDto.setQualification(librarian.getQualification());
//        librarianProfileDto.setDesignation(librarian.getDesignation());
//        librarianProfileDto.setExperienceYears(librarianProfileDto.getExperienceYears());
//        librarianProfileDto.setJoinedOn(librarian.getJoinedOn());
//        librarianProfileDto.setStatus(librarian.getStatus());
//        if (librarian.getLibrary() != null) {
//
//            librarianProfileDto.setLibraryId(librarian.getLibrary().getId());
//            librarianProfileDto.setLibraryName(librarian.getLibrary().getUser().getName());
//            librarianProfileDto.setWebsite(librarian.getLibrary().getWebsite());
//        }

        return librarianProfileDto;
    }

    // -------------------------------------------------------
// SEARCH available librarians (not yet hired by any library)
// -------------------------------------------------------

    public Page<LibrarianSearchResponseDto> searchAvailableLibrarians(
            String query,
            Double minExp,
            Integer maxAge,
            Pageable pageable) {

        return libraianDao.searchAvailableLibrarians(
                query,
                minExp,
                maxAge,
                LibrarianProfileStatus.ACTIVE,
                pageable
        ).map(this::toSearchResponse);
    }

    // -------------------------------------------------------
// HIRE a librarian — links them to this library
// -------------------------------------------------------
    public LibrarianSearchResponseDto hireLibrarian(Integer librarianId, Integer libraryId) {

        Librarian librarian = libraianDao.findById(librarianId)
                .orElseThrow(() -> new RuntimeException("Librarian not found: " + librarianId));

//        if (librarian.getLibrary() != null) {
//            throw new RuntimeException(
//                    librarian.getUser().getName() + " is already hired by another library.");
//        }

        Library library = libraryDao.findById(libraryId)
                .orElseThrow(() -> new RuntimeException("Library not found"));

//        librarian.setLibrary(library);
//        librarian.setStatus(LibrarianProfileStatus.ACTIVE);
//        librarian.setJoinedOn(LocalDate.now());

        libraianDao.save(librarian);
        return toSearchResponse(librarian);
    }

    // -------------------------------------------------------
// MAPPER
// -------------------------------------------------------
    private LibrarianSearchResponseDto toSearchResponse(Librarian librarian) {
        User user = librarian.getUser();

        int age = 0;
        if (user.getDob() != null) {
            age = Period.between(user.getDob(), LocalDate.now()).getYears();
        }

//        List<String> specialties = (librarian.getSpecialties() != null &&
//                !librarian.getSpecialties().isBlank())
//                ? Arrays.asList(librarian.getSpecialties().split(","))
//                : List.of();

        return LibrarianSearchResponseDto.builder()
//                .librarianId(librarian.getLibrarianId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getContact())
                .photo(user.getProfilePic())
                .age(age)
//                .experienceYears(librarian.getExperienceYears())
//                .qualification(librarian.getQualification())
//                .location(user.getAddress())
//                .employeeCode(librarian.getEmployeeCode())
//                .specialties(specialties)
//                .status(librarian.getStatus())
//                .hired(librarian.getLibrary() != null)
                .build();
    }

}
