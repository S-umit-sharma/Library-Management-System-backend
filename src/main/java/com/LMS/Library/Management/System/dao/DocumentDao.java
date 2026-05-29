package com.LMS.Library.Management.System.dao;


import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.entities.UserDocument;
import com.LMS.Library.Management.System.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentDao extends JpaRepository<UserDocument,Integer> {

    Optional<UserDocument> findByUserAndDocumentType(User user, DocumentType documentType);
}
