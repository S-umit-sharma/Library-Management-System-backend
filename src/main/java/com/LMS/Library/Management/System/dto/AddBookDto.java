package com.LMS.Library.Management.System.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AddBookDto {

    private String title;

    private String author;

    private String isbn;

    private Double price;

    private Integer stock;

    private String category;

    private String language;

    private String description;

    private MultipartFile file;
}
