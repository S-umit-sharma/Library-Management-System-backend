package com.LMS.Library.Management.System.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeskStatsDto {
    private long totalBooksIssued;      // currently issued (not returned)
    private long overdueCount;          // issued past dueDate
    private double totalDues;           // sum of all membership dueAmounts
    private long lowStockCount;         // books with stock <= lowStockThreshold
}
