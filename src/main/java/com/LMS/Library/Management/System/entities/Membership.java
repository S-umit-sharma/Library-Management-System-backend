package com.LMS.Library.Management.System.entities;

import com.LMS.Library.Management.System.enums.MembershipStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(
        name = "memberships",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"library_id", "user_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer membershipId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id", nullable = false)
    private Library library;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private MembershipPlan membershipPlan;

    @Column(nullable = false, unique = true)
    private String membershipNumber;


    @Column(nullable = false)
    private LocalDate issueDate;

    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    private MembershipStatus status;

    private Double amountPaid;

    private Double dueAmount;

    private Integer booksIssued;

    private LocalDate lastRenewedDate;

    private String remarks;

    private Double membershipFee;

    private Integer maxBooksAllowed;

}