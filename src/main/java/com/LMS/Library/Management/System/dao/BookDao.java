package com.LMS.Library.Management.System.dao;

import com.LMS.Library.Management.System.dto.BookReponseDto;
import com.LMS.Library.Management.System.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BookDao extends JpaRepository<Book,Integer> {


    @Query(
            "SELECT b FROM Book b " +
                    "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%',:keyword,'%')) " +
                    "OR LOWER (b.author) LIKE LOWER(CONCAT('%',:keyword,'%'))" +
                    "OR LOWER (b.category) LIKE LOWER(CONCAT('%',:keyword,'%'))" +
                    "OR LOWER (b.isbn) LIKE LOWER(CONCAT('%',:keyword,'%'))"

    )
    Page<Book> searchBook(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
        SELECT new com.LMS.Library.Management.System.dto.BookReponseDto(
            b.bookId,
            b.title,
            b.author,
            b.isbn,
            b.price,
            b.stock,
            b.category,
            b.language,
            b.description,
            b.coverImage,
            p.publisherId,
            u.name,
            b.createdAt,
            b.updatedAt
        )
        FROM Book b
        JOIN b.publisher p
        JOIN p.user u
        WHERE p.publisherId = :publisherId
        """)
    Page<BookReponseDto> findBooksByPublisherId(
            @Param("publisherId") Integer publisherId,
            Pageable pageable
    );


}
