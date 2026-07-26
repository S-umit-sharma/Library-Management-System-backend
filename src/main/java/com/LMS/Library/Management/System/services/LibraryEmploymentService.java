package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dao.*;
import com.LMS.Library.Management.System.dto.*;
import com.LMS.Library.Management.System.entities.*;
import com.LMS.Library.Management.System.enums.EmploymentStatus;
import com.LMS.Library.Management.System.utils.EmployeeCodeGenrator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryEmploymentService {

    private final LibraryEmploymentDao employmentDao;
    private final LibraianDao librarianDao;
    private final LibraryDao libraryDao;

    // ------------------------------------------------------------------
    // HIRE — create a new LibraryEmployment record
    // Called when library confirms hiring a librarian
    // ------------------------------------------------------------------
    @Transactional
    public LibraryEmploymentResponseDto hireLibrarian(
            Integer profileId, Integer libraryId, HireLibrarianRequestDto request) {

        Library library = libraryDao.findById(libraryId)
                .orElseThrow(() -> new RuntimeException("Library not found"));

        Librarian librarian = librarianDao.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Librarian profile not found: " + profileId));

        // Block if already actively employed here
        if (employmentDao.existsByLibrary_IdAndLibrarianLibrarianIdAndEmploymentStatus(libraryId, profileId, EmploymentStatus.ACTIVE)) {
            throw new RuntimeException(
                    librarian.getUser().getName() + " is already actively employed at your library.");
        }

        // Block if not available for hire
        if (Boolean.FALSE.equals(librarian.getAvailableForHire())) {
            throw new RuntimeException(
                    librarian.getUser().getName() + " is not currently available for hire.");
        }

        // Generate unique employee code per library
        String employeeCode = generateUniqueEmployeeCode(library, librarian);

        LocalDate joinedOn = request.getJoinedOn() != null
                ? request.getJoinedOn()
                : LocalDate.now();

        LibraryEmployment employment = new LibraryEmployment();
        employment.setLibrary(library);
        employment.setLibrarian(librarian);
        employment.setEmployeeCode(employeeCode);
        employment.setDesignation(request.getDesignation());
        employment.setSalary(request.getSalary());
        employment.setJoinedOn(joinedOn);
        employment.setEmploymentStatus(EmploymentStatus.ACTIVE);

        employmentDao.save(employment);

        // Mark librarian as no longer available
        librarian.setAvailableForHire(false);
        librarianDao.save(librarian);

        return toResponse(employment);
    }

    // ------------------------------------------------------------------
    // GET ALL STAFF — active employments for a library (paginated)
    // ------------------------------------------------------------------
    public Page<LibraryEmploymentResponseDto> getActiveStaff(
            Integer libraryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("joinedOn").descending());
        return employmentDao
                .findByLibrary_IdAndEmploymentStatus(libraryId, EmploymentStatus.ACTIVE, pageable)
                .map(this::toResponse);
    }

    // ------------------------------------------------------------------
    // GET ALL STAFF — all statuses (full history for the library)
    // ------------------------------------------------------------------
    public Page<LibraryEmploymentResponseDto> getAllStaff(
            Integer libraryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("joinedOn").descending());
        return employmentDao.findByLibrary_Id(libraryId, pageable)
                .map(this::toResponse);
    }

    // ------------------------------------------------------------------
    // GET SINGLE EMPLOYMENT
    // ------------------------------------------------------------------
    public LibraryEmploymentResponseDto getEmployment(
            Integer employmentId, Integer libraryId) {
        LibraryEmployment employment = employmentDao
                .findByEmploymentIdAndLibrary_Id(employmentId, libraryId)
                .orElseThrow(() -> new RuntimeException("Employment record not found"));
        return toResponse(employment);
    }

    // ------------------------------------------------------------------
    // UPDATE — change designation or salary
    // ------------------------------------------------------------------
    @Transactional
    public LibraryEmploymentResponseDto updateEmployment(
            Integer employmentId, Integer libraryId, UpdateEmploymentRequest request) {

        LibraryEmployment employment = employmentDao
                .findByEmploymentIdAndLibrary_Id(employmentId, libraryId)
                .orElseThrow(() -> new RuntimeException("Employment record not found"));

        if (employment.getEmploymentStatus() != EmploymentStatus.ACTIVE) {
            throw new RuntimeException("Cannot update a non-active employment record.");
        }

        if (request.getDesignation() != null && !request.getDesignation().isBlank()) {
            employment.setDesignation(request.getDesignation());
        }
        if (request.getSalary() != null) {
            employment.setSalary(request.getSalary());
        }

        return toResponse(employmentDao.save(employment));
    }

    // ------------------------------------------------------------------
    // END EMPLOYMENT — mark as RESIGNED / TERMINATED
    // ------------------------------------------------------------------
    @Transactional
    public LibraryEmploymentResponseDto endEmployment(
            Integer employmentId, Integer libraryId, EmploymentStatus reason) {

        if (reason == EmploymentStatus.ACTIVE || reason == EmploymentStatus.ON_LEAVE) {
            throw new RuntimeException(
                    "End employment reason must be RESIGNED or TERMINATED.");
        }

        LibraryEmployment employment = employmentDao
                .findByEmploymentIdAndLibrary_Id(employmentId, libraryId)
                .orElseThrow(() -> new RuntimeException("Employment record not found"));

        if (employment.getEmploymentStatus() != EmploymentStatus.ACTIVE) {
            throw new RuntimeException("This employment is already ended.");
        }

        employment.setEmploymentStatus(reason);
        employment.setLeftOn(LocalDate.now());
        employmentDao.save(employment);

        // Mark librarian as available for hire again
        Librarian librarian = employment.getLibrarian();
        librarian.setAvailableForHire(true);
        librarianDao.save(librarian);

        return toResponse(employment);
    }

    // ------------------------------------------------------------------
    // EMPLOYMENT HISTORY — for a specific librarian profile
    // ------------------------------------------------------------------
    public Page<LibraryEmploymentResponseDto> getLibrarianHistory(
            Integer profileId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("joinedOn").descending());
        return employmentDao.findByLibrarianLibrarianId(profileId, pageable)
                .map(this::toResponse);
    }

    // ------------------------------------------------------------------
    // INTERNALS
    // ------------------------------------------------------------------
    private String generateUniqueEmployeeCode(Library library, Librarian librarian) {
        String base = EmployeeCodeGenrator.genrateEmpCode(
                librarian.getUser().getName(),
                librarian.getLibrarianId());
        String code = base;
        int suffix = 1;
        // ensure uniqueness per library
        while (employmentDao.existsByLibrary_IdAndEmployeeCode(library.getId(), code)) {
            code = base + suffix++;
        }
        return code;
    }

    private LibraryEmploymentResponseDto toResponse(LibraryEmployment e) {
        Librarian librarian = e.getLibrarian();
        User user = librarian.getUser();

        int age = 0;
        if (user.getDob() != null) {
            age = Period.between(user.getDob(), LocalDate.now()).getYears();
        }

        return LibraryEmploymentResponseDto.builder()
                .employmentId(e.getEmploymentId())
                .librarianId(librarian.getLibrarianId())
                .librarianName(user.getName())
                .librarianEmail(user.getEmail())
                .librarianContact(user.getContact())
                .profilePic(user.getProfilePic())
                .highestQualification(librarian.getHighestQualification())
                .totalExperienceYears(librarian.getTotalExperienceYears())
                .specialization(librarian.getSpecialization())
                .preferredDesignation(librarian.getPreferredDesignation())
                .employeeCode(e.getEmployeeCode())
                .designation(e.getDesignation())
                .salary(e.getSalary())
                .joinedOn(e.getJoinedOn())
                .leftOn(e.getLeftOn())
                .employmentStatus(e.getEmploymentStatus())
                .libraryId(e.getLibrary().getId())
                .libraryName(e.getLibrary().getUser().getName())
                .build();
    }

    public List<StaffDto> getStaffOnDuty(Integer libraryId) {

        List<LibraryEmployment> employments =
                employmentDao.findByLibrary_IdAndEmploymentStatus(
                        libraryId,
                        EmploymentStatus.ACTIVE
                );

        return employments.stream()
                .map(this::toStaffDto)
                .toList();
    }

    private StaffDto toStaffDto(LibraryEmployment employment) {

        String name = employment.getLibrarian().getUser().getName();

        String initials = Arrays.stream(name.split(" "))
                .filter(s -> !s.isBlank())
                .map(s -> s.substring(0, 1).toUpperCase())
                .reduce("", String::concat);

        return StaffDto.builder()
                .initials(initials)
                .name(name)
                .role(employment.getDesignation())
                .onDuty(true)
                .bg("#EEEDFE")
                .fg("#3C3489")
                .build();
    }
}