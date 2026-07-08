package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dao.LibraryDao;
import com.LMS.Library.Management.System.dao.MembershipDao;
import com.LMS.Library.Management.System.dao.MembershipPlanDao;
import com.LMS.Library.Management.System.dao.UserDao;
import com.LMS.Library.Management.System.dto.CreateMembershipRequest;
import com.LMS.Library.Management.System.dto.LibraryMembershipResponseDto;
import com.LMS.Library.Management.System.entities.Library;
import com.LMS.Library.Management.System.entities.Membership;
import com.LMS.Library.Management.System.entities.MembershipPlan;
import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.enums.MembershipStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public  class MembershipsImpl implements MembershipService {


    private final MembershipDao membershipRepository;
    private final LibraryDao libraryRepository;
    private final UserDao userRepository;
    private final MembershipPlanDao membershipPlanDao;

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

        //  Finding the Library in the DB
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() ->
                        new RuntimeException("Library not found"));

        // Finding the MembershipPlan in the DB
        MembershipPlan membershipPlan = membershipPlanDao.findById(dto.getPlanId()).orElseThrow(() -> new RuntimeException("Membership plan no longer available or not found"));
        System.out.println(membershipPlan.getPlanId() +"");
        System.out.println(membershipPlan.getFee() +"");


        if (membershipRepository.existsByLibraryAndUser(library, user)) {
            throw new RuntimeException("Membership already exists.");
        }

        LocalDate issueDate = LocalDate.now();
        LocalDate expiryDate =  issueDate.plusDays(membershipPlan.getDurationDays());
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

    private String generateMembershipNumber() {


        return "MEM" + membershipRepository.countAll() + 1;
    }
}
