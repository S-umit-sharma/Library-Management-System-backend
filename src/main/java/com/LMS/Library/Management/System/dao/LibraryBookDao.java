package com.LMS.Library.Management.System.dao;

import com.LMS.Library.Management.System.entities.Book;
import com.LMS.Library.Management.System.entities.Library;
import com.LMS.Library.Management.System.entities.LibraryBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibraryBookDao extends JpaRepository<LibraryBook,Integer> {
    Optional<LibraryBook> findByLibraryAndBook(Library library, Book book);

    @Query("""
        SELECT lb
        FROM LibraryBook lb
        WHERE lb.library.id = :libraryId
        AND (
            LOWER(lb.book.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(lb.book.author) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(lb.book.isbn) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(lb.book.category) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    """)
    Page<LibraryBook> searchBooks(Integer libraryId,
                                  String keyword,
                                  Pageable pageable);
}
