package com.LMS.Library.Management.System.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipSummaryDto {

    private String label;

    private Integer count;

    private String color;

    private Integer pct;
}