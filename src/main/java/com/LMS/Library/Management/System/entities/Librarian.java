package com.LMS.Library.Management.System.entities;

import com.LMS.Library.Management.System.enums.LibrarianStatus;
import com.LMS.Library.Management.System.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Librarian {

    @Id
    @GeneratedValue(strategy =
            GenerationType.IDENTITY)
    private Integer librarianId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne

    @JoinColumn(name = "library_id")
    private Library library;

    private String employeeCode;

    private String designation;

    private LocalDate joinedOn;

    private LocalDate leftOn;

    private Double salary;

    private Integer experienceYears;

    private String qualification;

    @Enumerated(EnumType.STRING)
    private LibrarianStatus status;

}