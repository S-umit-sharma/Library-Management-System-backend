package com.LMS.Library.Management.System.dao;

import com.LMS.Library.Management.System.entities.Library;
import com.LMS.Library.Management.System.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibraryDao extends JpaRepository<Library,Integer> {

    Optional<Library> findByUser_UserId(Integer userId);



    Optional<Library> findByUser(User user);
}
