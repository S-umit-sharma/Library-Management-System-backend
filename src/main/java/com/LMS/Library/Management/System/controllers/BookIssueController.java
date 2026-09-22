package com.LMS.Library.Management.System.controllers;


import com.LMS.Library.Management.System.Security.CustomUserDetails;
import com.LMS.Library.Management.System.dao.LibraryDao;
import com.LMS.Library.Management.System.dto.*;
import com.LMS.Library.Management.System.entities.Library;
import com.LMS.Library.Management.System.services.BookIssueService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book-issue")
@RequiredArgsConstructor
public class BookIssueController {

    private final BookIssueService bookIssueService;
    private final LibraryDao libraryDao;

    // ------------------------------------------------------------------
    // POST /book-issue
    // Issue a book to a member
    // Body: { "membershipNumber": "MEM-0001", "bookId": 1 }
    // ------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<BookIssueResponseDto> issueBook(
            @Valid @RequestBody IssueBookRequest request,
            Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();

        Integer libraryId = libraryDao.findByUser_UserId(userId).orElseThrow(() -> new RuntimeException("User Not found")).getId();
        return ResponseEntity.ok(bookIssueService.issueBook(request, libraryId));
    }

    // ------------------------------------------------------------------
    // PATCH /book-issue/{issueId}/return
    // Return a book
    // ------------------------------------------------------------------
    @PatchMapping("/{issueId}/return")
    public ResponseEntity<BookIssueResponseDto> returnBook(
            @PathVariable Integer issueId,
            Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();

        Integer libraryId = libraryDao.findByUser_UserId(userId).orElseThrow(() -> new RuntimeException("User Not found")).getId();
        return ResponseEntity.ok(bookIssueService.returnBook(issueId, libraryId));
    }

    // ------------------------------------------------------------------
    // GET /book-issue?page=0&size=10
    // All issues for the library (paginated)
    // ------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<Page<BookIssueResponseDto>> getAllIssues(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();

        Integer libraryId = libraryDao.findByUser_UserId(userId).orElseThrow(() -> new RuntimeException("User Not found")).getId();
        return ResponseEntity.ok(bookIssueService.getAllIssues(libraryId, page, size));
    }

    // ------------------------------------------------------------------
    // GET /book-issue/current?page=0&size=10
    // Currently issued books (not returned)
    // ------------------------------------------------------------------
    @GetMapping("/current")
    public ResponseEntity<Page<BookIssueResponseDto>> getCurrentlyIssued(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();

        Integer libraryId = libraryDao.findByUser_UserId(userId).orElseThrow(() -> new RuntimeException("User Not found")).getId();
        return ResponseEntity.ok(
                bookIssueService.getCurrentlyIssuedBooks(libraryId, page, size));
    }

    // ------------------------------------------------------------------
    // GET /book-issue/overdue?page=0&size=10
    // Overdue issues — for Dues tab
    // ------------------------------------------------------------------
    @GetMapping("/overdue")
    public ResponseEntity<Page<BookIssueResponseDto>> getOverdue(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();

        Integer libraryId = libraryDao.findByUser_UserId(userId).orElseThrow(() -> new RuntimeException("User Not found")).getId();
        return ResponseEntity.ok(
                bookIssueService.getOverdueIssues(libraryId, page, size));
    }

    // ------------------------------------------------------------------
    // GET /book-issue/member/{membershipNumber}
    // Find member by membership number + all their currently issued books
    // Used by MemberLookupTab
    // ------------------------------------------------------------------
    @GetMapping("/member/{membershipNumber}")
    public ResponseEntity<MemberIssueDetailsDto> getMemberIssueDetails(
            @PathVariable String membershipNumber, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();

        Integer libraryId = libraryDao.findByUser_UserId(userId).orElseThrow(() -> new RuntimeException("User Not found")).getId();
        return ResponseEntity.ok(
                bookIssueService.getMemberIssueDetails(membershipNumber, libraryId));
    }

    // ------------------------------------------------------------------
    // GET /book-issue/stats
    // Desk stat cards: issued count, overdue count, total dues, low stock
    // ------------------------------------------------------------------
    @GetMapping("/stats")
    public ResponseEntity<DeskStatsDto> getDeskStats(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();

        Integer libraryId = libraryDao.findByUser_UserId(userId).orElseThrow(() -> new RuntimeException("User Not found")).getId();
        return ResponseEntity.ok(bookIssueService.getDeskStats(libraryId));
    }

    // ------------------------------------------------------------------
    // GET /book-issue/low-stock?page=0&size=10
    // Low stock books — for LowStockTab
    // ------------------------------------------------------------------
    @GetMapping("/low-stock")
    public ResponseEntity<?> getLowStockBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();

        Integer libraryId = libraryDao.findByUser_UserId(userId).orElseThrow(() -> new RuntimeException("User Not found")).getId();

        if (libraryId == null) ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Again");
        return ResponseEntity.ok(
                bookIssueService.getLowStockBooks(libraryId, page, size));
    }

    @GetMapping("/issued/count")
    public ResponseEntity<Long> getIssuedBookCount(
            Authentication authentication) {

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Integer userId = userDetails.getUserId();

        Library library =
                libraryDao.findByUser_UserId(userId).orElseThrow(()-> new RuntimeException("User Not Found"));

        long count =
                bookIssueService.getCurrentlyIssuedCount(
                        library.getId()
                );

        return ResponseEntity.ok(count);
    }
}

