package com.LMS.Library.Management.System.dao;

import com.LMS.Library.Management.System.entities.LibraryEmployment;
import com.LMS.Library.Management.System.enums.EmploymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface LibraryEmploymentDao extends JpaRepository<LibraryEmployment, Integer> {

    boolean existsByLibrary_IdAndLibrarianLibrarianId(
            Integer libraryId,
            Integer librarianId);

    // All active employments under a library (paginated)
    Page<LibraryEmployment> findByLibrary_IdAndEmploymentStatus(
            Integer libraryId, EmploymentStatus status, Pageable pageable);

    // All employments under a library (all statuses)
    Page<LibraryEmployment> findByLibrary_Id(
            Integer libraryId, Pageable pageable);

    // Employment history of a specific librarian
    Page<LibraryEmployment> findByLibrarianLibrarianId(
            Integer profileId, Pageable pageable);

    // Check if librarian is already actively employed at this library
    boolean existsByLibrary_IdAndLibrarianLibrarianIdAndEmploymentStatus(
            Integer libraryId, Integer profileId, EmploymentStatus status);

    // Find specific active employment record (for end employment / update)
    Optional<LibraryEmployment> findByEmploymentIdAndLibrary_Id(
            Integer employmentId, Integer libraryId);

    // Count active staff under a library
    long countByLibrary_IdAndEmploymentStatus(
            Integer libraryId, EmploymentStatus status);

    // Check employee code uniqueness per library
    boolean existsByLibrary_IdAndEmployeeCode(
            Integer libraryId, String employeeCode);
}
