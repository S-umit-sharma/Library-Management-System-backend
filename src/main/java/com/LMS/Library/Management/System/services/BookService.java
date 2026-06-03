package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dao.BookDao;
import com.LMS.Library.Management.System.dao.PublisherDao;
import com.LMS.Library.Management.System.dto.AddBookDto;
import com.LMS.Library.Management.System.dto.BookReponseDto;
import com.LMS.Library.Management.System.dto.BookUpdateDto;
import com.LMS.Library.Management.System.entities.Book;
import com.LMS.Library.Management.System.entities.Publisher;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.function.Consumer;

@Service
public class BookService {

    @Autowired
    PublisherDao publisherRepository;

    private static final String UPLOAD_DIR =
            "uploads/books";

    @Autowired
    BookDao bookDao;

    public void addBook(AddBookDto dto, Integer userId) {

        Publisher publisher = publisherRepository
                .findByUser_UserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("Publisher not found"));

        Book book = new Book();

        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setIsbn(dto.getIsbn());
        book.setCategory(dto.getCategory());
        book.setLanguage(dto.getLanguage());
        book.setDescription(dto.getDescription());
        book.setPrice(dto.getPrice());
        book.setStock(dto.getStock());

        // Save image and get filename/path
        String imagePath = uploadBookImage(dto.getFile());

        book.setCoverImage(imagePath);

        book.setPublisher(publisher);


        book.setCreatedAt(LocalDateTime.now());

        bookDao.save(book);
    }

    @Transactional
    public void updateBook(Integer id,BookUpdateDto dto){
        Book book = bookDao.findById(id).orElseThrow(()-> new RuntimeException("Book Not Found"));

        updateIfNotNull(dto.getTitle(),book::setTitle);
        updateIfNotNull(dto.getAuthor(),book::setAuthor);
        updateIfNotNull(dto.getIsbn(),book::setIsbn);
        updateIfNotNull(dto.getPrice(),book::setPrice);
        updateIfNotNull(dto.getStock(),book::setStock);
        updateIfNotNull(dto.getCategory(),book::setCategory);
        updateIfNotNull(dto.getLanguage(),book::setLanguage);
        updateIfNotNull(dto.getDescription(),book::setDescription);

        if(dto.getCoverImage() != null && !dto.getCoverImage().isEmpty()){
            try{
                Files.deleteIfExists(Paths.get(book.getCoverImage()));
            }catch(IOException e){
                e.printStackTrace();
            }

            String imagePath = uploadBookImage(dto.getCoverImage());
            book.setCoverImage(imagePath);
        }
        book.setUpdatedAt(LocalDateTime.now());
        bookDao.save(book);
    }


    @Transactional
    public String uploadBookImage(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Please select a book image");
        }
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) directory.mkdirs();
        Path path = Paths.get(UPLOAD_DIR, fileName);
        try {
//           Files.deleteIfExists(Paths.get(user.getProfilePic()));
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException ioException) {
            throw new RuntimeException("Document Uplaod Failed");
        }

        return path.toString();
    }

    private <T> void updateIfNotNull(T value, Consumer<T> setter){
        if(value != null){
            setter.accept(value);
        }
    }

    public BookReponseDto getBook(Integer id) {
        Book book = bookDao.findById(id).orElseThrow(()-> new RuntimeException("Book Not Found"));


        BookReponseDto bookReponseDto = BookReponseDto.builder()
                .bookId(book.getBookId())
                .isbn(book.getIsbn())
                .price(book.getPrice())
                .author(book.getAuthor())
                .stock(book.getStock())
                .title(book.getTitle())
                .category(book.getCategory())
                .coverImage(book.getCoverImage())
                .createdAt(book.getCreatedAt())
                .description(book.getDescription())
                .language(book.getLanguage())
                .publisherId(book.getPublisher().getPublisherId())
                .publisherName(book.getPublisher().getUser().getName())
                .updatedAt(book.getUpdatedAt())
                .build();

        return bookReponseDto;
    }
}

