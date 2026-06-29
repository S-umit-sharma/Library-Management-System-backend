package com.LMS.Library.Management.System.dto;

import com.LMS.Library.Management.System.entities.Publisher;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookReponseDto {

    private Integer bookId;

    private String title;

    private String author;

    private String isbn;

    private Double price;

    private Integer stock;

    private String category;

    private String language;

    private String description;

    private String coverImage;

    private Integer publisherId;

    private String publisherName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
