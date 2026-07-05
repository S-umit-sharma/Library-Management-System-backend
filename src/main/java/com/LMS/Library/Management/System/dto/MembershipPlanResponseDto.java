package com.LMS.Library.Management.System.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipPlanResponseDto {

    private Integer planId;

    private String planName;

    private Integer durationDays;

    private Double fee;

    private Integer maxBooksAllowed;

    private Boolean active;

    private String description;
}