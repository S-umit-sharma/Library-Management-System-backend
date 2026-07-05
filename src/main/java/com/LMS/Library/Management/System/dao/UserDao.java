package com.LMS.Library.Management.System.dao;

import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User,Integer> {


    Optional<User> findByEmail(String email);
    List<User> findByUserType(UserType role);
}
