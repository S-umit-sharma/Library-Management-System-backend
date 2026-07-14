package com.LMS.Library.Management.System.services;


import com.LMS.Library.Management.System.dao.*;
import com.LMS.Library.Management.System.dto.*;
import com.LMS.Library.Management.System.entities.*;
import com.LMS.Library.Management.System.enums.IssueStatus;
import com.LMS.Library.Management.System.enums.MembershipStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookIssueService {

    private final BookIssueRepository bookIssueRepository;
    private final BookDao bookRepository;
    private final MembershipDao membershipRepository;
    private final LibraryDao libraryRepository;
    private final LibraryBookDao libraryBookDao;

    private static final int LOW_STOCK_THRESHOLD = 3;

    // ------------------------------------------------------------------
    // ISSUE A BOOK
    // ------------------------------------------------------------------
    @Transactional
    public BookIssueResponseDto issueBook(IssueBookRequest request, Integer libraryId) {

        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new RuntimeException("Library not found"));

        // Find membership by membershipNumber under this library
        Membership membership = membershipRepository
                .findByMembershipNumberAndLibraryId(request.getMembershipNumber(), libraryId)
                .orElseThrow(() -> new RuntimeException(
                        "No active membership found with ID: " + request.getMembershipNumber()));

        // Membership must be ACTIVE
        if (membership.getStatus() != MembershipStatus.ACTIVE) {
            throw new RuntimeException("Membership is " + membership.getStatus() +
                    ". Only ACTIVE memberships can issue books.");
        }

        // Check membership due limit (library sets this via depositAmount)
        if (membership.getDueAmount() != null &&
                membership.getDueAmount() >= library.getDepositAmount()) {
            throw new RuntimeException(
                    "Member has ₹" + membership.getDueAmount() + " in dues. " +
                            "Dues must be cleared before issuing new books.");
        }

        // Check max books allowed
        int currentlyIssued = membership.getBooksIssued() == null ? 0 : membership.getBooksIssued();
        int maxAllowed = membership.getMaxBooksAllowed() == null ? 3 : membership.getMaxBooksAllowed();
        if (currentlyIssued >= maxAllowed) {
            throw new RuntimeException(
                    "Member has already issued " + currentlyIssued + " book(s). " +
                            "Maximum allowed is " + maxAllowed + ".");
        }

        // Find book
        LibraryBook libraryBook = libraryBookDao.findByLibrary_IdAndBookBookId(libraryId, request.getBookId())
                .orElseThrow(() ->
                        new RuntimeException("Book is not available in this library."));
        ;

        if (libraryBook.getQuantity() <= 0) {
            throw new RuntimeException("Book is out of stock.");
        }

        // Check if this book is already issued to this member
        bookIssueRepository.findByBookBookIdAndMembershipMembershipIdAndStatus(
                        libraryBook.getBook().getBookId(), membership.getMembershipId(), IssueStatus.ISSUED)
                .ifPresent(existing -> {
                    throw new RuntimeException(
                            "This book is already issued to this member.");
                });

        // Calculate due date using library's bookIssueDays setting
        int issueDays = library.getBookIssueDays() == null ? 14 : library.getBookIssueDays();
        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(issueDays);

        // Create issue record
        BookIssue issue = BookIssue.builder()
                .book(libraryBook.getBook())
                .membership(membership)
                .library(library)
                .issueDate(issueDate)
                .dueDate(dueDate)
                .status(IssueStatus.ISSUED)
                .fineDue(0.0)
                .build();

        bookIssueRepository.save(issue);

        // Decrement book stock
        libraryBook.setQuantity(libraryBook.getQuantity() - 1);
        libraryBookDao.save(libraryBook);

        // Increment booksIssued on membership
        membership.setBooksIssued(currentlyIssued + 1);
        membershipRepository.save(membership);

        return toResponse(issue);
    }

    // ------------------------------------------------------------------
    // RETURN A BOOK
    // ------------------------------------------------------------------
    @Transactional
    public BookIssueResponseDto returnBook(Integer issueId, Integer libraryId) {

        BookIssue issue = bookIssueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue record not found: " + issueId));

        // Ownership check
        if (!issue.getLibrary().getId().equals(libraryId)) {
            throw new RuntimeException("This issue does not belong to your library.");
        }

        if (issue.getStatus() == IssueStatus.RETURNED) {
            throw new RuntimeException("This book has already been returned.");
        }

        Library library = issue.getLibrary();
        LocalDate today = LocalDate.now();
        LocalDate dueDate = issue.getDueDate();

        // Calculate fine
        double fineDue = 0.0;
        if (today.isAfter(dueDate)) {
            long overdueDays = ChronoUnit.DAYS.between(dueDate, today);
            int lateFine = library.getLateFine() == null ? 0 : library.getLateFine();
            fineDue = overdueDays * lateFine;
        }

        // Update issue
        issue.setStatus(IssueStatus.RETURNED);
        issue.setReturnDate(today);
        issue.setFineDue(fineDue);
        bookIssueRepository.save(issue);

        LibraryBook libraryBook = libraryBookDao
                .findByLibrary_IdAndBookBookId(
                        libraryId,
                        issue.getBook().getBookId())
                .orElseThrow(() ->
                        new RuntimeException("Library book not found."));

        libraryBook.setQuantity(
                libraryBook.getQuantity() + 1);

        libraryBookDao.save(libraryBook);

        // Decrement booksIssued on membership & add fine to dueAmount
        Membership membership = issue.getMembership();
        int currentIssued = membership.getBooksIssued() == null ? 0 : membership.getBooksIssued();
        membership.setBooksIssued(Math.max(currentIssued - 1, 0));

        if (fineDue > 0) {
            double existingDue = membership.getDueAmount() == null ? 0.0 : membership.getDueAmount();
            membership.setDueAmount(existingDue + fineDue);
        }
        membershipRepository.save(membership);

        return toResponse(issue);
    }

    // ------------------------------------------------------------------
    // GET ALL ISSUES FOR LIBRARY (paginated)
    // ------------------------------------------------------------------
    public Page<BookIssueResponseDto> getAllIssues(Integer libraryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return bookIssueRepository.findByLibraryId(libraryId, pageable)
                .map(this::toResponse);
    }

    // ------------------------------------------------------------------
    // GET CURRENTLY ISSUED BOOKS (paginated)
    // ------------------------------------------------------------------
    public Page<BookIssueResponseDto> getCurrentlyIssuedBooks(
            Integer libraryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dueDate").ascending());
        return bookIssueRepository.findByLibraryIdAndStatus(
                        libraryId, IssueStatus.ISSUED, pageable)
                .map(this::toResponse);
    }

    // ------------------------------------------------------------------
    // GET OVERDUE ISSUES (paginated) — for Dues tab
    // ------------------------------------------------------------------
    public Page<BookIssueResponseDto> getOverdueIssues(
            Integer libraryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dueDate").ascending());
        return bookIssueRepository.findOverdueByLibrary(
                        libraryId, LocalDate.now(), pageable)
                .map(this::toResponse);
    }

    // ------------------------------------------------------------------
    // FIND MEMBER BY MEMBERSHIP NUMBER — for MemberLookupTab
    // ------------------------------------------------------------------
    public MemberIssueDetailsDto getMemberIssueDetails(
            String membershipNumber, Integer libraryId) {

        Membership membership = membershipRepository
                .findByMembershipNumberAndLibraryId(membershipNumber, libraryId)
                .orElseThrow(() -> new RuntimeException(
                        "No membership found with number: " + membershipNumber));

        List<BookIssue> activeIssues = bookIssueRepository
                .findByMembershipMembershipIdAndStatus(
                        membership.getMembershipId(), IssueStatus.ISSUED);

        User user = membership.getUser();

        return MemberIssueDetailsDto.builder()
                .memberName(user.getName())
                .memberEmail(user.getEmail())
                .memberContact(user.getContact())
                .membershipNumber(membership.getMembershipNumber())
                .membershipId(membership.getMembershipId())
                .dueAmount(membership.getDueAmount() == null ? 0.0 : membership.getDueAmount())
                .booksCurrentlyIssued(membership.getBooksIssued() == null ? 0 : membership.getBooksIssued())
                .maxBooksAllowed(membership.getMaxBooksAllowed() == null ? 3 : membership.getMaxBooksAllowed())
                .currentlyIssuedBooks(activeIssues.stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    // ------------------------------------------------------------------
    // DESK STATS — for the 4 stat cards
    // ------------------------------------------------------------------
    public DeskStatsDto getDeskStats(Integer libraryId) {

        long totalIssued = bookIssueRepository
                .countByLibraryIdAndStatus(libraryId, IssueStatus.ISSUED);

        long overdueCount = bookIssueRepository
                .countOverdueByLibrary(libraryId, LocalDate.now());

        // Total dues = sum of all membership dueAmounts under this library
        Double totalDues = membershipRepository.sumDueAmountByLibraryId(libraryId);

        long lowStockCount =
                libraryBookDao.countByLibrary_IdAndQuantityLessThanEqual(
                        libraryId,
                        LOW_STOCK_THRESHOLD);

        return DeskStatsDto.builder()
                .totalBooksIssued(totalIssued)
                .overdueCount(overdueCount)
                .totalDues(totalDues == null ? 0.0 : totalDues)
                .lowStockCount(lowStockCount)
                .build();
    }

    // ------------------------------------------------------------------
    // LOW STOCK BOOKS — for LowStockTab
    // ------------------------------------------------------------------
    public Page<LowStockBookDto> getLowStockBooks(Integer libraryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("quantity").ascending());


        Page<LibraryBook> libraryBook = libraryBookDao.findByLibrary_IdAndQuantityLessThanEqual(
                libraryId, 3, pageable);


        return libraryBook.map(lb -> LowStockBookDto.builder()
                .bookId(lb.getBook().getBookId())
                .title(lb.getBook().getTitle())
                .author(lb.getBook().getAuthor())
                .coverImage(lb.getBook().getCoverImage())
                .stock(lb.getQuantity())
                .build());

    }

    // ------------------------------------------------------------------
    // MAPPER
    // ------------------------------------------------------------------
    private BookIssueResponseDto toResponse(BookIssue issue) {
        Book book = issue.getBook();
        Membership membership = issue.getMembership();
        User user = membership.getUser();
        LocalDate today = LocalDate.now();
        LocalDate dueDate = issue.getDueDate();

        boolean overdue = issue.getStatus() == IssueStatus.ISSUED
                && today.isAfter(dueDate);
        long overdueDays = overdue ? ChronoUnit.DAYS.between(dueDate, today) : 0;

        return BookIssueResponseDto.builder()
                .issueId(issue.getIssueId())
                .bookId(book.getBookId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .coverImage(book.getCoverImage())
                .memberName(user.getName())
                .memberEmail(user.getEmail())
                .memberContact(user.getContact())
                .membershipNumber(membership.getMembershipNumber())
                .membershipId(membership.getMembershipId())
                .issueDate(issue.getIssueDate())
                .dueDate(issue.getDueDate())
                .returnDate(issue.getReturnDate())
                .status(issue.getStatus())
                .fineDue(issue.getFineDue())
                .overdue(overdue)
                .overdueDays(overdueDays)
                .build();
    }
}