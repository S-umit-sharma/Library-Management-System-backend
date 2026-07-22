package com.LMS.Library.Management.System.dto;

import com.LMS.Library.Management.System.enums.LibrarianProfileStatus;
import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LibrarianSearchResponseDto {

    private Integer librarianId;
    private String name;
    private String email;
    private String phone;
    private String photo;
    private Integer age;
    private Integer experienceYears;
    private String qualification;
    private String location;
    private String employeeCode;
    private List<String> specialties;
    private LibrarianProfileStatus status;
    private boolean hired; // true if library is not null
}