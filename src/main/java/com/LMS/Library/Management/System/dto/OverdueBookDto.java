package com.LMS.Library.Management.System.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverdueBookDto {

    private String name;

    private String book;

    private Integer days;
}