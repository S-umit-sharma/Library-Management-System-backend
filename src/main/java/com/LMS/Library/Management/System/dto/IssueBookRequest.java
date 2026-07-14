package com.LMS.Library.Management.System.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IssueBookRequest {

    @NotNull(message = "Membership number is required")
    private String membershipNumber;   // e.g. MEM-0001

    @NotNull(message = "Book ID is required")
    private Integer bookId;
}
