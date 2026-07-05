package com.LMS.Library.Management.System.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MembershipPlanRequestDto {

    private String planName;

    private Integer durationDays;

    private Double fee;

    private Integer maxBooksAllowed;

    private String description;
}
