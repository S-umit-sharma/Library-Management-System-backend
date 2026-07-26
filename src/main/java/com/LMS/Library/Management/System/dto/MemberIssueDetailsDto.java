package com.LMS.Library.Management.System.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberIssueDetailsDto {

    private String memberName;
    private String memberEmail;
    private String memberContact;

    private String membershipNumber;
    private Integer membershipId;

    // Membership level due
    private Double membershipFine;

    // Sum of all book fines
    private Double bookFine;

    // membershipFine + bookFine
    private Double totalFine;

    private Integer booksCurrentlyIssued;
    private Integer maxBooksAllowed;

    // Books issued with individual fine
    private List<BookIssueResponseDto> currentlyIssuedBooks;
}