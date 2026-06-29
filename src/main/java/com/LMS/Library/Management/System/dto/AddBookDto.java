package com.LMS.Library.Management.System.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AddBookDto {

    @NotNull(message = "Enter the title")
    private String title;

    @NotNull(message = "Enter the author    ")
    private String author;

    @NotNull(message = "Enter the isbn")
    private String isbn;

    @NotNull(message = "Enter the price")
    private Double price;

    @NotNull(message = "Enter the stock")
    private Integer stock;

    @NotNull(message = "Enter the category")
    private String category;

    @NotNull(message = "Enter the language")
    private String language;

    @NotNull(message = "Enter the description")
    private String description;

    @NotNull(message = "Choose a book image")
    private MultipartFile file;
}
