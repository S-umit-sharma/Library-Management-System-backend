package com.LMS.Library.Management.System.dao;

import com.LMS.Library.Management.System.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDao extends JpaRepository<Book,Integer> {
}
