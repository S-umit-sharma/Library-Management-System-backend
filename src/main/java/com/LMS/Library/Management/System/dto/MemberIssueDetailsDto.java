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
    private Double dueAmount;           // from membership.dueAmount
    private Integer booksCurrentlyIssued;
    private Integer maxBooksAllowed;

    private List<BookIssueResponseDto> currentlyIssuedBooks;
}