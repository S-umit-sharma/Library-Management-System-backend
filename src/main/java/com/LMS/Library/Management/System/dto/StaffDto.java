package com.LMS.Library.Management.System.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffDto {

    private String initials;

    private String name;

    private String role;

    private boolean onDuty;

    private String bg;

    private String fg;
}