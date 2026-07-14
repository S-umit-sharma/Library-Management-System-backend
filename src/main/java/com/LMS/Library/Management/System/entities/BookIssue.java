package com.LMS.Library.Management.System.entities;


import com.LMS.Library.Management.System.enums.IssueStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_issues")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer issueId;

    // Which book was issued
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // Issued against which membership
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_id", nullable = false)
    private Membership membership;

    // Which library issued it (for easy querying)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id", nullable = false)
    private Library library;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate dueDate;         // issueDate + library.bookIssueDays

    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssueStatus status;        // ISSUED, RETURNED

    private Double fineDue;            // calculated on return: overdueDays * library.lateFine

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}