package com.LMS.Library.Management.System.dao;

import com.LMS.Library.Management.System.entities.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibrayDao extends JpaRepository<Library,Integer> {
}
