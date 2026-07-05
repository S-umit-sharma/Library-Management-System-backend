package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dao.*;
import com.LMS.Library.Management.System.dto.LibraryBookResponseDto;
import com.LMS.Library.Management.System.entities.Book;
import com.LMS.Library.Management.System.entities.Library;
import com.LMS.Library.Management.System.entities.LibraryBook;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LibraryBookService {

    @Autowired
    private LibraryDao librayDao;

    @Autowired
    private LibraryBookDao libraryBookDao;

    @Autowired
    private BookService bookService;


    @Autowired
    private BookDao bookDao;

    @Transactional
    public void addBookToLibrary(Integer id, Integer loggedInUser, Integer quantity) {
        if (quantity == null || quantity <= 0) throw new RuntimeException("Quantity must be greatet than zero");
        Library library = librayDao.findByUser_UserId(loggedInUser).orElseThrow(() -> new RuntimeException("User Not Found"));
        Book book = bookDao.findById(id).orElseThrow(() -> new RuntimeException("Book Not Found"));


        if (book.getStock() < quantity) throw new IllegalArgumentException("Insufficient stock");

        Optional<LibraryBook> existing = libraryBookDao.findByLibraryAndBook(library, book);

        if (existing.isPresent()) {
            LibraryBook lb = existing.get();
            lb.setQuantity(lb.getQuantity() + quantity);
            libraryBookDao.save(lb);
        } else {
            LibraryBook lb = LibraryBook.builder().library(library).book(book).quantity(quantity).build();

            libraryBookDao.save(lb);
        }

        book.setStock(book.getStock() - quantity);

        bookDao.save(book);
    }

    @Transactional
    public void removeBookFromLibrary(Integer bookId, Integer userId, Integer quantity) {

        Library library = librayDao.findByUser_UserId(userId).orElseThrow(() -> new RuntimeException("Library not found"));

        Book book = bookDao.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

        LibraryBook libraryBook = libraryBookDao.findByLibraryAndBook(library, book).orElseThrow(() -> new RuntimeException("Book not available in library"));

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        if (libraryBook.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough copies in library");
        }

        if (libraryBook.getQuantity().equals(quantity)) {
            libraryBookDao.delete(libraryBook);
        } else {
            libraryBook.setQuantity(libraryBook.getQuantity() - quantity);
        }
    }


    public Page<LibraryBookResponseDto> searchBooks(Integer libraryId, String keyword, int page, int size) {

        Page<LibraryBook> books = libraryBookDao.searchBooks(libraryId, keyword, PageRequest.of(page, size));
        System.out.println(books);
        return books.map(lb -> LibraryBookResponseDto.builder()
                .libraryBookId(lb.getId())
                .bookId(lb.getBook().getBookId())
                .title(lb.getBook().getTitle())
                .author(lb.getBook().getAuthor())
                .isbn(lb.getBook().getIsbn())
                .category(lb.getBook().getCategory())
                .language(lb.getBook().getLanguage())
                .coverImage(lb.getBook().getCoverImage())
                .quantity(lb.getQuantity())
                .publisherName(lb.getBook().getPublisher().getUser().getName())
                .build());
    }
}
