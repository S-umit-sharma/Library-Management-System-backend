package com.LMS.Library.Management.System.entities;

import com.LMS.Library.Management.System.enums.EmploymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "library_id",
                "employeeCode"
        })
})
public class LibraryEmployment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id", nullable = false)
    private Library library;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "librarian", nullable = false)
    private Librarian librarian;

    @Column(nullable = false)
    private String employeeCode;

    @Column(nullable = false)
    private String designation;

    private Double salary;

    private LocalDate joinedOn;

    private LocalDate leftOn;

    @Enumerated(EnumType.STRING)
    private EmploymentStatus employmentStatus;
}