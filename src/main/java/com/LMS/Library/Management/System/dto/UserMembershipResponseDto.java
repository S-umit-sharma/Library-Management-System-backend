package com.LMS.Library.Management.System.dto;

import com.LMS.Library.Management.System.entities.MembershipPlan;
import com.LMS.Library.Management.System.enums.MembershipStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMembershipResponseDto {


    private String membershipNumber;
    private String memberName;
    private String memberEmail;
    private String libraryName;
    private MembershipPlan plan;
    private int durationMonths;
    private double fee;
    private double amountPaid;
    private double dueAmount;          // fee - amountPaid
    private LocalDate startDate;
    private LocalDate endDate;
    private MembershipStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

