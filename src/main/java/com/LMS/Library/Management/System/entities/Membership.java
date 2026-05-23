package com.LMS.Library.Management.System.entities;

import com.LMS.Library.Management.System.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "memberships")
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer membershipId;
    @ManyToOne
    @JoinColumn(name="library_id")
    private Library library;
    private LocalDate joinedOn;
    private LocalDate leftOn;
    private Integer currentDues = 0;
    private Integer memberId;
    @Enumerated(EnumType.STRING)
    private Status memberStatusId;

    @ManyToOne
    @JoinColumn(name="student_id")
    private Student student;

}
