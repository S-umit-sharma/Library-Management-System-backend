package com.LMS.Library.Management.System.dao;

import com.LMS.Library.Management.System.entities.Book;
import com.LMS.Library.Management.System.entities.Library;
import com.LMS.Library.Management.System.entities.LibraryBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibraryBookDao extends JpaRepository<LibraryBook,Integer> {
    Optional<LibraryBook> findByLibraryAndBook(Library library, Book book);
}
