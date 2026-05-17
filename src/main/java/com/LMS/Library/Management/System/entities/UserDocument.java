package com.LMS.Library.Management.System.entities;

import com.LMS.Library.Management.System.enums.DocumentType;
import com.LMS.Library.Management.System.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_documents")
@Getter
@Setter
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
