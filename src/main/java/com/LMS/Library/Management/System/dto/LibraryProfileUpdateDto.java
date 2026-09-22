package com.LMS.Library.Management.System.dto;
import com.LMS.Library.Management.System.enums.Gender;
import lombok.Data;
import java.time.LocalDate;

@Data
public class LibraryProfileUpdateDto {

        // User fields
        private String name;
        private String email;
        private String contact;
        private LocalDate dob;
        private String address;
        private Gender gender;

        // Librarian fields
        private String highestQualification;
        private Double totalExperienceYears;
        private String specialization;
        private String certifications;
        private String preferredDesignation;
        private String bio;

}
