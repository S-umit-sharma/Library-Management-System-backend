package com.LMS.Library.Management.System.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MembershipPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer planId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id", nullable = false)
    private Library library;

    @Column(nullable = false)
    private String planName;

    @Column(nullable = false)
    private Integer durationDays;

    @Column(nullable = false)
    private Double fee;

    @Column(nullable = false)
    private Integer maxBooksAllowed;

    @Column(nullable = false)
    private Boolean active;

    private String description;
}

