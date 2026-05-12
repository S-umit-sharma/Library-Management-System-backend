package com.LMS.Library.Management.System.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_documents")
public class UserDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userDocumentId;

    private String documentPath;

    private LocalDateTime uploadedOn;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "document_type_id")
    private DocumentType documentType;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "status_id")
    private Status status;
}
