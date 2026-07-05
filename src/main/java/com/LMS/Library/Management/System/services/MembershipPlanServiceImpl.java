package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dao.LibraryDao;
import com.LMS.Library.Management.System.dao.MembershipPlanDao;
import com.LMS.Library.Management.System.dto.MembershipPlanRequestDto;
import com.LMS.Library.Management.System.dto.MembershipPlanResponseDto;
import com.LMS.Library.Management.System.entities.Library;
import com.LMS.Library.Management.System.entities.MembershipPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembershipPlanServiceImpl implements MembershipPlanService {

    private final MembershipPlanDao membershipPlanRepository;
    private final LibraryDao libraryRepository;

    @Override
    public MembershipPlanResponseDto createPlan(
            MembershipPlanRequestDto dto,
            Integer libraryId) {

        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() ->
                        new RuntimeException("Library not found"));

        if (membershipPlanRepository.existsByLibraryAndPlanNameIgnoreCase(
                library,
                dto.getPlanName())) {

            throw new RuntimeException("Plan already exists.");
        }

        MembershipPlan plan = new MembershipPlan();

        plan.setLibrary(library);
        plan.setPlanName(dto.getPlanName());
        plan.setDurationDays(dto.getDurationDays());
        plan.setFee(dto.getFee());
        plan.setMaxBooksAllowed(dto.getMaxBooksAllowed());
        plan.setDescription(dto.getDescription());
        plan.setActive(true);

        MembershipPlan saved =
                membershipPlanRepository.save(plan);

        return MembershipPlanResponseDto.builder()
                .planId(saved.getPlanId())
                .planName(saved.getPlanName())
                .durationDays(saved.getDurationDays())
                .fee(saved.getFee())
                .maxBooksAllowed(saved.getMaxBooksAllowed())
                .active(saved.getActive())
                .description(saved.getDescription())
                .build();
    }

    @Override
    public Page<MembershipPlanResponseDto> getPlans(
            Integer libraryId,
            int page,
            int size) {

        return membershipPlanRepository
                .findByLibraryId(
                        libraryId,
                        PageRequest.of(page, size))
                .map(plan -> MembershipPlanResponseDto.builder()
                        .planId(plan.getPlanId())
                        .planName(plan.getPlanName())
                        .durationDays(plan.getDurationDays())
                        .fee(plan.getFee())
                        .maxBooksAllowed(plan.getMaxBooksAllowed())
                        .active(plan.getActive())
                        .description(plan.getDescription())
                        .build());
    }
}