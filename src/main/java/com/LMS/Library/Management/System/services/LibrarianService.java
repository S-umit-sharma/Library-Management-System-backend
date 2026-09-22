package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dao.LibrarianDao;
import com.LMS.Library.Management.System.dao.LibraryDao;
import com.LMS.Library.Management.System.dao.LibraryEmploymentDao;
import com.LMS.Library.Management.System.dao.UserDao;
import com.LMS.Library.Management.System.dto.LibrarianDetailDto;
import com.LMS.Library.Management.System.dto.LibrarianProfileResponseDto;
import com.LMS.Library.Management.System.dto.LibrarianSearchResponseDto;
import com.LMS.Library.Management.System.dto.LibraryProfileUpdateDto;
import com.LMS.Library.Management.System.entities.Librarian;
import com.LMS.Library.Management.System.entities.Library;
import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.enums.LibrarianProfileStatus;
import com.LMS.Library.Management.System.enums.Status;
import com.LMS.Library.Management.System.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class LibrarianService {
    @Autowired
    UserService userService;

    @Autowired
    LibrarianDao libraianDao;

    @Autowired
    LibraryDao libraryDao;

    @Autowired
    LibraryEmploymentDao libraryEmploymentDao;

    @Autowired
    UserDao userDao;


    public void addDetails(LibrarianDetailDto librarianDto) {
        User user = userService.findUserByEmail(librarianDto.getEmail());
        if (user == null) throw new RuntimeException("User Not Found");
        if (user.getStatus() == Status.PENDING) throw new RuntimeException("Verify OTP then try again");
        Librarian librarian = new Librarian();
        librarian.setHighestQualification(librarianDto.getHighestQualification());
        librarian.setTotalExperienceYears(librarianDto.getTotalExperienceYears());
        librarian.setSpecialization(librarianDto.getSpecialization());
        librarian.setCertifications(librarianDto.getCertifications());
        librarian.setPreferredDesignation(librarianDto.getPreferredDesignation());
        librarian.setBio(librarianDto.getBio());
        librarian.setUser(user);
        libraianDao.save(librarian);
        user.setStatus(Status.ACTIVE);
        userService.saveUser(user);

    }

    public LibrarianProfileResponseDto getLibrarianProfile(Integer userId) {

        // Get logged-in user
        User user = userService.findUserById(userId);

        if (user == null) {
            throw new RuntimeException("Invalid logged-in user");
        }

        // Verify user type
        if (user.getUserType() != UserType.LIBRARIAN) {
            throw new RuntimeException("User is not a librarian");
        }

        // Get librarian profile
        Librarian librarian = libraianDao.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Librarian profile not completed yet"));

        LibrarianProfileResponseDto librarianProfileDto =
                new LibrarianProfileResponseDto();

        // =========================
        // User Details
        // =========================

        librarianProfileDto.setUserId(user.getUserId());
        librarianProfileDto.setName(user.getName());
        librarianProfileDto.setEmail(user.getEmail());
        librarianProfileDto.setContact(user.getContact());
        librarianProfileDto.setDob(user.getDob());
        librarianProfileDto.setAddress(user.getAddress());
        librarianProfileDto.setProfilePic(user.getProfilePic());
        librarianProfileDto.setGender(user.getGender());
        librarianProfileDto.setUserType(user.getUserType());
        librarianProfileDto.setStatus(user.getStatus());

        // =========================
        // Librarian Details
        // =========================

        librarianProfileDto.setLibrarianId(
                librarian.getLibrarianId()
        );


        librarianProfileDto.setHighestQualification(
                librarian.getHighestQualification()
        );

        librarianProfileDto.setTotalExperienceYears(librarian.getTotalExperienceYears());
        librarianProfileDto.setSpecialization(librarian.getSpecialization());
        librarianProfileDto.setCertifications(librarian.getCertifications());
        librarianProfileDto.setPreferredDesignation(librarian.getPreferredDesignation());
        librarianProfileDto.setBio(librarian.getBio());
        librarianProfileDto.setAvailableForHire(librarian.getAvailableForHire());
        librarianProfileDto.setProfileStatus(librarian.getProfileStatus());


        // =========================
        // Library Details
        // =========================
//
//        if (librarian.getEmploymentHistory(). != null) {
//
//            librarianProfileDto.setLibraryId(
//                    librarian.getLibrary().getId()
//            );
//
//            librarianProfileDto.setLibraryName(
//                    librarian.getLibrary().getUser().getName()
//            );
//
//            librarianProfileDto.setWebsite(
//                    librarian.getLibrary().getWebsite()
//            );
//
//        } else {
//
//            // Librarian is currently not hired
//            librarianProfileDto.setLibraryId(null);
//            librarianProfileDto.setLibraryName(null);
//            librarianProfileDto.setWebsite(null);
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
            Pageable pageable, Integer libraryId) {

        return libraianDao.searchAvailableLibrarians(
                query,
                minExp,
                maxAge,
                LibrarianProfileStatus.ACTIVE,
                pageable
        ).map(librarian->toSearchResponse(librarian,libraryId));
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
        return toSearchResponse(librarian,libraryId);
    }

    // -------------------------------------------------------
// MAPPER
// -------------------------------------------------------
    private LibrarianSearchResponseDto toSearchResponse(Librarian librarian, Integer libraryId) {
        User user = librarian.getUser();

        int age = 0;
        if (user.getDob() != null) {
            age = Period.between(user.getDob(), LocalDate.now()).getYears();
        }

        boolean hired = libraryEmploymentDao
                .existsByLibrary_IdAndLibrarianLibrarianId(
                        libraryId,
                        librarian.getLibrarianId());

        return LibrarianSearchResponseDto.builder()
                .librarianId(librarian.getLibrarianId())
                .name(user.getName())
                .email(user.getEmail())
                .contact(user.getContact())
                .profilePic(user.getProfilePic())
                .age(age)
                .location(user.getCity().getName())
                .highestQualification(librarian.getHighestQualification())
                .totalExperienceYears(librarian.getTotalExperienceYears())
                .specialization(librarian.getSpecialization())
                .preferredDesignation(librarian.getPreferredDesignation())
                .profileStatus(librarian.getProfileStatus())
                .availableForHire(librarian.getAvailableForHire())
                .hired(hired)
                .build();
    }

    public Librarian findByUserId(Integer userId) {

        return libraianDao.findByUser_UserId(userId).orElseThrow(()-> new RuntimeException("User Not Found"));
    }

    public void updateProfile(LibraryProfileUpdateDto dto,Integer userId) {
        User user = userService.findUserById(userId);

        Librarian librarian = libraianDao.findByUser_UserId(userId).orElseThrow(()->new RuntimeException("Librarian not found"));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setContact(dto.getContact());
        user.setDob(dto.getDob());
        user.setAddress(dto.getAddress());
        user.setGender(dto.getGender());

        librarian.setHighestQualification(dto.getHighestQualification());
        librarian.setTotalExperienceYears(dto.getTotalExperienceYears());
        librarian.setSpecialization(dto.getSpecialization());
        librarian.setCertifications(dto.getCertifications());
        librarian.setPreferredDesignation(dto.getPreferredDesignation());
        librarian.setBio(dto.getBio());

         userDao.save(user);
         libraianDao.save(librarian);

    }
}
