package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.dto.MembershipPlanRequestDto;
import com.LMS.Library.Management.System.dto.MembershipPlanResponseDto;
import com.LMS.Library.Management.System.services.MembershipPlanService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/membership-plan")
@RequiredArgsConstructor
public class MembershipPlanController {

    private final MembershipPlanService membershipPlanService;

    @PostMapping
    public ResponseEntity<MembershipPlanResponseDto> createPlan(
            @RequestBody MembershipPlanRequestDto dto,
            HttpSession session) {

        Integer libraryId =
                (Integer) session.getAttribute("libraryId");

        return ResponseEntity.ok(
                membershipPlanService.createPlan(
                        dto,
                        libraryId));
    }

    @GetMapping
    public ResponseEntity<Page<MembershipPlanResponseDto>> getPlans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {

        Integer libraryId =
                (Integer) session.getAttribute("libraryId");

        return ResponseEntity.ok(
                membershipPlanService.getPlans(
                        libraryId,
                        page,
                        size));
    }
}