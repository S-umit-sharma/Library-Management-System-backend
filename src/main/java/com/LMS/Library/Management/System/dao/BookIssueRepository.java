package com.LMS.Library.Management.System.dao;


import com.LMS.Library.Management.System.entities.BookIssue;
import com.LMS.Library.Management.System.enums.IssueStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookIssueRepository extends JpaRepository<BookIssue, Integer> {

    // All issues for a library (paginated)
    Page<BookIssue> findByLibraryId(Integer libraryId, Pageable pageable);

    // Currently issued (not returned) for a library
    Page<BookIssue> findByLibraryIdAndStatus(
            Integer libraryId, IssueStatus status, Pageable pageable);

    // All issues for a specific membership
    List<BookIssue> findByMembershipMembershipIdAndStatus(
            Integer membershipId, IssueStatus status);

    // Overdue issues for a library
    @Query("""
    SELECT bi
    FROM BookIssue bi
    WHERE bi.library.id = :libraryId
    AND bi.status = com.LMS.Library.Management.System.enums.IssueStatus.RETURNED 
    AND bi.dueDate < :today
""")
    Page<BookIssue> findOverdueByLibrary_id(
            @Param("libraryId") Integer libraryId,
            @Param("today") LocalDate today,
            Pageable pageable);



    // Count currently issued books for a library (for stat card)
    long countByLibraryIdAndStatus(Integer libraryId, IssueStatus status);

    // Count overdue for a library (for stat card)
    @Query("""
        SELECT COUNT(bi) FROM BookIssue bi
        WHERE bi.library.id = :libraryId
        AND bi.status = 'ISSUED'
        AND bi.dueDate < :today
    """)
    long countOverdueByLibrary(
            @Param("libraryId") Integer libraryId,
            @Param("today") LocalDate today);

    // Check if a specific book is already issued to a membership and not returned
    Optional<BookIssue> findByBookBookIdAndMembershipMembershipIdAndStatus(
            Integer bookId, Integer membershipId, IssueStatus status);

    @Query("""
SELECT COUNT(b)
FROM BookIssue b
WHERE b.library.id=:libraryId
AND b.issueDate=CURRENT_DATE
""")
    Integer countIssuedToday(Integer libraryId);

    @Query("""
SELECT COUNT(b)
FROM BookIssue b
WHERE b.library.id=:libraryId
AND b.status='ISSUED'
AND b.dueDate<CURRENT_DATE
""")
    Integer countOverdueBooks(Integer libraryId);

    List<BookIssue> findByMembershipMembershipId(Integer membershipId);

    List<BookIssue> findByMembershipMembershipIdAndFineDueGreaterThanOrderByIssueDateAsc(
            Integer membershipId,
            Double fineDue);

}