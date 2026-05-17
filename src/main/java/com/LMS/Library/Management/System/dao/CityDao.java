package com.LMS.Library.Management.System.dao;

import com.LMS.Library.Management.System.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityDao extends JpaRepository<City, Long> {

}
