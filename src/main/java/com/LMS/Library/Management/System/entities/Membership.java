package com.LMS.Library.Management.System.entities;

import com.LMS.Library.Management.System.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "students")
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer membershipId;
    private Library library;
    private LocalDate joinedOn;
    private LocalDate leftOn;
    private Integer currentDues = 0;
    private Integer memberId;
    @Enumerated(EnumType.STRING)
    private Status memberStatusId;
}
