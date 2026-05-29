package com.LMS.Library.Management.System.entities;

import com.LMS.Library.Management.System.enums.DocumentType;
import com.LMS.Library.Management.System.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType documentType;

    @Column(nullable = false)
    private String documentNumber;

    // Stored image/file name
    @Column(nullable = false)
    private String fileName;

    // Original uploaded file name
    private String originalFileName;

    // File type like image/png, application/pdf
    private String contentType;

    // File size in bytes
    private Long fileSize;

    // File storage path or URL
    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    public void prePersist() {
        this.uploadedAt = LocalDateTime.now();
    }
}
