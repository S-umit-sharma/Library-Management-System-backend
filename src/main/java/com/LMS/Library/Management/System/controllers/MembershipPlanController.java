package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.Security.CustomUserDetails;
import com.LMS.Library.Management.System.dao.LibraryDao;
import com.LMS.Library.Management.System.dto.MembershipPlanRequestDto;
import com.LMS.Library.Management.System.dto.MembershipPlanResponseDto;
import com.LMS.Library.Management.System.services.MembershipPlanService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/membership-plan")
@RequiredArgsConstructor
public class MembershipPlanController {

    private final MembershipPlanService membershipPlanService;
    private final LibraryDao libraryDao;

    @PostMapping
    public ResponseEntity<MembershipPlanResponseDto> createPlan(
            @RequestBody MembershipPlanRequestDto dto,
            Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();
        Integer libraryId = libraryDao.findByUser_UserId(userId).orElseThrow(()->new RuntimeException("User Not found")).getId();


        return ResponseEntity.ok(
                membershipPlanService.createPlan(
                        dto,
                        libraryId));
    }

    @GetMapping
    public ResponseEntity<Page<MembershipPlanResponseDto>> getPlans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();
        Integer libraryId = libraryDao.findByUser_UserId(userId).orElseThrow(()->new RuntimeException("User Not found")).getId();


        return ResponseEntity.ok(
                membershipPlanService.getPlans(
                        libraryId,
                        page,
                        size));
    }
}