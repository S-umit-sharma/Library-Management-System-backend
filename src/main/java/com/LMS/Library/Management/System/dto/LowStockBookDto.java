package com.LMS.Library.Management.System.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LowStockBookDto {
    private Integer bookId;
    private String title;
    private String author;
    private String coverImage;
    private Integer stock;
}