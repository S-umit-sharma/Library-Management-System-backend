package com.LMS.Library.Management.System.dao;

import com.LMS.Library.Management.System.entities.Library;
import com.LMS.Library.Management.System.entities.Membership;
import com.LMS.Library.Management.System.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MembershipDao extends JpaRepository<Membership,Integer> {

    boolean existsByLibraryAndUser(
            Library library,
            User user);

    Page<Membership> findByLibraryId(
            Integer libraryId,
            Pageable pageable);

    @Query("""
            SELECT m
            FROM Membership m
            WHERE m.library.id = :libraryId
            AND (
                LOWER(m.user.name) LIKE LOWER(CONCAT('%',:keyword,'%'))
                OR LOWER(m.user.email) LIKE LOWER(CONCAT('%',:keyword,'%'))
                OR LOWER(m.membershipNumber) LIKE LOWER(CONCAT('%',:keyword,'%'))
            )
            """)
    Page<Membership> searchMembership(
            Integer libraryId,
            String keyword,
            Pageable pageable);

    @Query("SELECT COUNT(*) FROM Membership")
    int countAll();
}