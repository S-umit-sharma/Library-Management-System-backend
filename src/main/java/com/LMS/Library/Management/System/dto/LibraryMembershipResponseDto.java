package com.LMS.Library.Management.System.dto;

import com.LMS.Library.Management.System.enums.MembershipStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LibraryMembershipResponseDto {

    private String membershipNumber;

    // Member Details
    private String memberName;
    private String memberEmail;
    private String memberContact;

    // Plan Details
    private String planName;

    // Membership Details
    private LocalDate issueDate;
    private LocalDate expiryDate;

    private MembershipStatus status;

    private Double membershipFee;
    private Double amountPaid;
    private Double dueAmount;

    private Integer booksIssued;
    private Integer maxBooksAllowed;

    private String remarks;
}