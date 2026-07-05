package com.LMS.Library.Management.System.dao;

import com.LMS.Library.Management.System.entities.Library;
import com.LMS.Library.Management.System.entities.MembershipPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipPlanDao extends JpaRepository<MembershipPlan,Integer> {
    boolean existsByLibraryAndPlanNameIgnoreCase(Library library, String planName);

    Page<MembershipPlan> findByLibraryId(Integer libraryId, Pageable pageable);
}
