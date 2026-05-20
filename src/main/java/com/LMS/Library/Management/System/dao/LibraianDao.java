package com.LMS.Library.Management.System.dao;

import com.LMS.Library.Management.System.entities.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraianDao extends JpaRepository<Librarian, Integer> {
}
