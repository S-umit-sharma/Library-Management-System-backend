package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dao.*;
import com.LMS.Library.Management.System.entities.Book;
import com.LMS.Library.Management.System.entities.Library;
import com.LMS.Library.Management.System.entities.LibraryBook;
import com.LMS.Library.Management.System.entities.Publisher;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LibraryBookService {

    @Autowired
    private LibrayDao librayDao;

    @Autowired
    private LibraryBookDao libraryBookDao;

    @Autowired
    private BookService bookService;


    @Autowired
    private BookDao bookDao;

    @Transactional
    public void addBookToLibrary(Integer id, Integer loggedInUser, Integer quantity) {
        if(quantity == null || quantity <= 0) throw new RuntimeException("Quantity must be greatet than zero");
        Library library = librayDao.findByUser_UserId(loggedInUser).orElseThrow(() -> new RuntimeException("User Not Found"));
        Book book = bookDao.findById(id).orElseThrow(() -> new RuntimeException("Book Not Found"));


        if(book.getStock() < quantity)throw new IllegalArgumentException("Insufficient stock");

        Optional<LibraryBook> existing =
                libraryBookDao.findByLibraryAndBook(library, book);

        if (existing.isPresent()) {
            LibraryBook lb = existing.get();
            lb.setQuantity(lb.getQuantity() + quantity);
            libraryBookDao.save(lb);
        } else {
            LibraryBook lb = LibraryBook.builder()
                    .library(library)
                    .book(book)
                    .quantity(quantity)
                    .build();

            libraryBookDao.save(lb);
        }

        book.setStock(book.getStock() - quantity);

bookDao.save(book);
    }
}
