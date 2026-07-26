package com.LMS.Library.Management.System.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LibraryDashboardDto {

    // Statistics
    private Integer totalBooks;

    private Integer activeMembers;

    private Integer issuedToday;

    private Integer overdueReturns;

    // Recent Activity
    private List<ActivityDto> recentActivities;

    // Overdue Books
    private List<OverdueBookDto> overdueBooks;

    // Staff On Duty
    private List<StaffDto> staffOnDuty;

    // Membership Summary
    private List<MembershipSummaryDto> membershipSummary;
}