package com.LMS.Library.Management.System.dao;

import com.LMS.Library.Management.System.entities.Publisher;
import com.LMS.Library.Management.System.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublisherDao extends JpaRepository<Publisher,Integer> {
    Publisher findByUser(User user);

    Optional<Publisher> findByUser_UserId(Integer userId);
}
