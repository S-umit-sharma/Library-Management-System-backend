package com.LMS.Library.Management.System.dao;


import com.LMS.Library.Management.System.entities.UserDocument;
import com.LMS.Library.Management.System.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentDao extends JpaRepository<UserDocument,Integer> {
}
