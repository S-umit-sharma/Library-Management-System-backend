package com.LMS.Library.Management.System.dao;

import com.LMS.Library.Management.System.entities.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipDao extends JpaRepository<Membership,Integer> {

}
