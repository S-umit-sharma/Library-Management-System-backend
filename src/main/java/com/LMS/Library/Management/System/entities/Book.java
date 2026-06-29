package com.LMS.Library.Management.System.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name="books")
@Getter
@Setter
@ToString
public class Book {

    @Id
    @GeneratedValue
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



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="publisher_id", nullable = false)
    private Publisher publisher;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
