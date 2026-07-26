package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dao.*;
import com.LMS.Library.Management.System.dto.CreateMembershipRequest;
import com.LMS.Library.Management.System.dto.LibraryMembershipResponseDto;
import com.LMS.Library.Management.System.dto.MembershipPaymentRequestDto;
import com.LMS.Library.Management.System.entities.*;
import com.LMS.Library.Management.System.enums.MembershipStatus;
import com.LMS.Library.Management.System.enums.UserType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MembershipsImpl implements MembershipService {


    private final MembershipDao membershipRepository;
    private final LibraryDao libraryRepository;
    private final UserDao userRepository;
    private final MembershipPlanDao membershipPlanDao;
    private final BookIssueRepository bookIssueRepository;



    private LibraryMembershipResponseDto mapToDto(Membership membership) {

        return LibraryMembershipResponseDto.builder()
                .membershipId(membership.getMembershipId())
                .membershipNumber(membership.getMembershipNumber())
                .memberName(membership.getUser().getName())
                .memberEmail(membership.getUser().getEmail())
                .memberContact(membership.getUser().getContact())
                .planName(membership.getMembershipPlan().getPlanName())
                .issueDate(membership.getIssueDate())
                .expiryDate(membership.getExpiryDate())
                .status(membership.getStatus())
                .membershipFee(membership.getMembershipFee())
                .amountPaid(membership.getAmountPaid())
                .dueAmount(membership.getDueAmount())
                .booksIssued(membership.getBooksIssued())
                .maxBooksAllowed(membership.getMaxBooksAllowed())
                .build();
    }

    @Override
    public LibraryMembershipResponseDto createMembership(
            CreateMembershipRequest dto,
            Integer libraryId) {

        //  Finding the User in the DB
        User user = userRepository.findByEmail(dto.getUserEmail())
                .orElseThrow(() ->
                        new RuntimeException("User Not Found"));

        if(user.getUserType() != UserType.STUDENT) throw new RuntimeException("Student Account Requierd");
        //  Finding the Library in the DB
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() ->
                        new RuntimeException("Library not found"));

        // Finding the MembershipPlan in the DB
        MembershipPlan membershipPlan = membershipPlanDao.findById(dto.getPlanId()).orElseThrow(() -> new RuntimeException("Membership plan no longer available or not found"));


        if (membershipRepository.existsByLibraryAndUser(library, user)) {
            throw new RuntimeException("Membership already exists.");
        }

        LocalDate issueDate = LocalDate.now();
        LocalDate expiryDate = issueDate.plusDays(membershipPlan.getDurationDays());
        Double dueAmount = membershipPlan.getFee() - dto.getAmountPaid();
        Membership membership = Membership.builder()
                .library(library)
                .user(user)
                .membershipNumber(generateMembershipNumber())
                .issueDate(issueDate)
                .expiryDate(expiryDate)
                .status(MembershipStatus.ACTIVE)
                .amountPaid(dto.getAmountPaid())
                .dueAmount(dueAmount)
                .booksIssued(0)
                .membershipPlan(membershipPlan)
                .membershipFee(membershipPlan.getFee())
                .maxBooksAllowed(membershipPlan.getMaxBooksAllowed())
                .build();

        membershipRepository.save(membership);

        return mapToDto(membership);
    }


    @Override
    public Page<LibraryMembershipResponseDto> getMemberships(
            Integer libraryId,
            int page,
            int size) {

        Page<Membership> memberships =
                membershipRepository.findByLibraryId(
                        libraryId,
                        PageRequest.of(page, size));

        return memberships.map(this::mapToDto);
    }

    @Override
    public Page<LibraryMembershipResponseDto> searchMemberships(
            Integer libraryId,
            String keyword,
            int page,
            int size) {

        Page<Membership> memberships =
                membershipRepository.searchMembership(
                        libraryId,
                        keyword,
                        PageRequest.of(page, size));

        return memberships.map(this::mapToDto);
    }

    @Override
    @Transactional
    public LibraryMembershipResponseDto payMembershipDue(
            Integer membershipId,
            MembershipPaymentRequestDto request,
            Integer libraryId) {

        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() ->
                        new RuntimeException("Membership not found."));

        if (!membership.getLibrary().getId().equals(libraryId)) {
            throw new RuntimeException("Unauthorized access.");
        }

        Double amount = request.getAmount();

        if (amount == null || amount <= 0) {
            throw new RuntimeException("Invalid payment amount.");
        }

        Double membershipDue = membership.getDueAmount() == null
                ? 0.0
                : membership.getDueAmount();

        if (amount > membershipDue) {
            throw new RuntimeException("Amount exceeds due amount.");
        }

        // Fetch all book issues having pending fine
        List<BookIssue> issues =
                bookIssueRepository
                        .findByMembershipMembershipIdAndFineDueGreaterThanOrderByIssueDateAsc(
                                membershipId,
                                0.0
                        );

        double remainingPayment = amount;

        for (BookIssue issue : issues) {

            if (remainingPayment <= 0) {
                break;
            }

            double fine = issue.getFineDue() == null
                    ? 0.0
                    : issue.getFineDue();

            if (fine <= 0) {
                continue;
            }

            if (remainingPayment >= fine) {

                // Clear complete fine of this book
                remainingPayment -= fine;
                issue.setFineDue(0.0);

            } else {

                // Partially clear fine
                issue.setFineDue(fine - remainingPayment);
                remainingPayment = 0;
            }

            bookIssueRepository.save(issue);
        }

        membership.setAmountPaid(
                (membership.getAmountPaid() == null ? 0.0 : membership.getAmountPaid())
                        + amount
        );

        membership.setDueAmount(membershipDue - amount);

        membershipRepository.save(membership);

        return mapToDto(membership);
    }
    private String generateMembershipNumber() {


        return "MEM" + membershipRepository.countAll() + 1;
    }
}
