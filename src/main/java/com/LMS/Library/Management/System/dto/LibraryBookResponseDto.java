package com.LMS.Library.Management.System.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LibraryBookResponseDto {

    private Integer libraryBookId;

    private Integer bookId;

    private String title;

    private String author;

    private String isbn;

    private String category;

    private String language;

    private String coverImage;

    private Integer quantity;

    private String publisherName;
}
