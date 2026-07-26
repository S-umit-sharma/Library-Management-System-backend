package com.LMS.Library.Management.System.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDto {

    private String color;

    private String text;

    private String time;
}