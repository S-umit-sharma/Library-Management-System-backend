package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dto.MembershipPlanRequestDto;
import com.LMS.Library.Management.System.dto.MembershipPlanResponseDto;
import org.springframework.data.domain.Page;

public interface MembershipPlanService {

    MembershipPlanResponseDto createPlan(
            MembershipPlanRequestDto dto,
            Integer libraryId);

    Page<MembershipPlanResponseDto> getPlans(
            Integer libraryId,
            int page,
            int size);
}