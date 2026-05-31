package com.LMS.Library.Management.System.entities;

import jakarta.persistence.*;

@Entity
@Table("books")
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

    @ManyToOne
    private Publisher publisher;
}
