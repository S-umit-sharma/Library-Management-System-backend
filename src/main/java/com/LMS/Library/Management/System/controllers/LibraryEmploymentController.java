package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.dto.*;
import com.LMS.Library.Management.System.enums.EmploymentStatus;
import com.LMS.Library.Management.System.services.LibraryEmploymentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employment")
@RequiredArgsConstructor
public class LibraryEmploymentController {

    private final LibraryEmploymentService employmentService;

    // POST /employment/{profileId}
    // Library hires a librarian — creates employment record
    @PostMapping("/{profileId}")
    public ResponseEntity<LibraryEmploymentResponseDto> hireLibrarian(
            @PathVariable Integer profileId,
            @Valid @RequestBody HireLibrarianRequestDto request,
            HttpSession session) {

        Integer libraryId = (Integer) session.getAttribute("libraryId");
        return ResponseEntity.ok(
                employmentService.hireLibrarian(profileId, libraryId, request));
    }

    // GET /employment/staff?page=0&size=10
    // All active staff for the library
    @GetMapping("/staff")
    public ResponseEntity<Page<LibraryEmploymentResponseDto>> getActiveStaff(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {

        Integer libraryId = (Integer) session.getAttribute("libraryId");
        return ResponseEntity.ok(
                employmentService.getActiveStaff(libraryId, page, size));
    }

    // GET /employment/staff/all?page=0&size=10
    // All staff including past (full history)
    @GetMapping("/staff/all")
    public ResponseEntity<Page<LibraryEmploymentResponseDto>> getAllStaff(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {

        Integer libraryId = (Integer) session.getAttribute("libraryId");
        return ResponseEntity.ok(
                employmentService.getAllStaff(libraryId, page, size));
    }

    // GET /employment/{employmentId}
    // Single employment record
    @GetMapping("/{employmentId}")
    public ResponseEntity<LibraryEmploymentResponseDto> getEmployment(
            @PathVariable Integer employmentId,
            HttpSession session) {

        Integer libraryId = (Integer) session.getAttribute("libraryId");
        return ResponseEntity.ok(
                employmentService.getEmployment(employmentId, libraryId));
    }

    // PATCH /employment/{employmentId}
    // Update designation or salary
    @PatchMapping("/{employmentId}")
    public ResponseEntity<LibraryEmploymentResponseDto> updateEmployment(
            @PathVariable Integer employmentId,
            @RequestBody UpdateEmploymentRequest request,
            HttpSession session) {

        Integer libraryId = (Integer) session.getAttribute("libraryId");
        return ResponseEntity.ok(
                employmentService.updateEmployment(employmentId, libraryId, request));
    }

    // PATCH /employment/{employmentId}/end?reason=RESIGNED
    // End an employment
    @PatchMapping("/{employmentId}/end")
    public ResponseEntity<LibraryEmploymentResponseDto> endEmployment(
            @PathVariable Integer employmentId,
            @RequestParam EmploymentStatus reason,
            HttpSession session) {

        Integer libraryId = (Integer) session.getAttribute("libraryId");
        return ResponseEntity.ok(
                employmentService.endEmployment(employmentId, libraryId, reason));
    }

    // GET /employment/librarian/{profileId}/history
    // Employment history of a librarian (all libraries)
    @GetMapping("/librarian/{profileId}/history")
    public ResponseEntity<Page<LibraryEmploymentResponseDto>> getLibrarianHistory(
            @PathVariable Integer profileId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                employmentService.getLibrarianHistory(profileId, page, size));
    }
}