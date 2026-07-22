package com.LMS.Library.Management.System.dao;

import com.LMS.Library.Management.System.entities.Librarian;
import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.enums.LibrarianProfileStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibraianDao extends JpaRepository<Librarian, Integer> {

    Optional<Librarian> findByUser(User user);

    Optional<Librarian> findByUserUserId(Integer userId);

    @Query("""
        SELECT l
        FROM Librarian l
        JOIN l.user u
        WHERE l.availableForHire = true
        AND l.profileStatus = :status
        AND (
            :query IS NULL OR :query = ''
            OR LOWER(u.name) LIKE LOWER(CONCAT('%', :query, '%'))
            OR LOWER(u.address) LIKE LOWER(CONCAT('%', :query, '%'))
            OR LOWER(l.highestQualification) LIKE LOWER(CONCAT('%', :query, '%'))
            OR LOWER(l.specialization) LIKE LOWER(CONCAT('%', :query, '%'))
        )
        AND (
            :minExp IS NULL
            OR l.totalExperienceYears >= :minExp
        )
       
        """)
    Page<Librarian> searchAvailableLibrarians(
            @Param("query") String query,
            @Param("minExp") Double minExp,
            @Param("maxAge") Integer maxAge,
            @Param("status") LibrarianProfileStatus status,
            Pageable pageable
    );
}