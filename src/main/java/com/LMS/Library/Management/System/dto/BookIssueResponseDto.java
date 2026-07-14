package com.LMS.Library.Management.System.dto;


import com.LMS.Library.Management.System.enums.IssueStatus;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookIssueResponseDto {

    private Integer issueId;

    // Book info
    private Integer bookId;
    private String title;
    private String coverImage;
    private String author;
    private String isbn;

    // Member info
    private String memberName;
    private String memberContact;
    private String memberEmail;
    private String membershipNumber;
    private Integer membershipId;

    // Issue info
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private IssueStatus status;
    private Double fineDue;

    // Overdue flag (derived, not stored)
    private boolean overdue;
    private long overdueDays;
}