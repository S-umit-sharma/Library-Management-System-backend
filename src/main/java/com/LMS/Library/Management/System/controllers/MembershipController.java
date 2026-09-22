package com.LMS.Library.Management.System.controllers;


import com.LMS.Library.Management.System.Security.CustomUserDetails;
import com.LMS.Library.Management.System.dao.LibraryDao;
import com.LMS.Library.Management.System.dto.CreateMembershipRequest;
import com.LMS.Library.Management.System.dto.LibraryMembershipResponseDto;
import com.LMS.Library.Management.System.dto.MembershipPaymentRequestDto;
import com.LMS.Library.Management.System.services.MembershipService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/membership")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;
    private final LibraryDao libraryDao;

    @PostMapping
    public ResponseEntity<LibraryMembershipResponseDto> createMembership(
            @RequestBody CreateMembershipRequest dto,
            Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();
        Integer libraryId = libraryDao.findByUser_UserId(userId).orElseThrow(() -> new RuntimeException("User Not found")).getId();


        return ResponseEntity.ok(
                membershipService.createMembership(dto, libraryId)
        );
    }

    @GetMapping
    public ResponseEntity<Page<LibraryMembershipResponseDto>> getAllMemberships(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();
        Integer libraryId = libraryDao.findByUser_UserId(userId).orElseThrow(() -> new RuntimeException("User Not found")).getId();


        return ResponseEntity.ok(
                membershipService.getMemberships(libraryId, page, size)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<Page<LibraryMembershipResponseDto>> searchMemberships(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();
        Integer libraryId = libraryDao.findByUser_UserId(userId).orElseThrow(() -> new RuntimeException("User Not found")).getId();


        return ResponseEntity.ok(
                membershipService.searchMemberships(libraryId, query, page, size)
        );
    }

    @PatchMapping("/{membershipId}/payment")
    public ResponseEntity<LibraryMembershipResponseDto> payMembershipDue(

            @PathVariable Integer membershipId,

            @RequestBody MembershipPaymentRequestDto request,

            Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = customUserDetails.getUserId();
        Integer libraryId = libraryDao.findByUser_UserId(userId).orElseThrow(() -> new RuntimeException("User Not found")).getId();


        return ResponseEntity.ok(

                membershipService.payMembershipDue(
                        membershipId,
                        request,
                        libraryId
                )

        );
    }


}