package com.LMS.Library.Management.System.controllers;


import com.LMS.Library.Management.System.dto.CreateMembershipRequest;
import com.LMS.Library.Management.System.dto.LibraryMembershipResponseDto;
import com.LMS.Library.Management.System.dto.MembershipPaymentRequestDto;
import com.LMS.Library.Management.System.services.MembershipService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/membership")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping
    public ResponseEntity<LibraryMembershipResponseDto> createMembership(
            @RequestBody CreateMembershipRequest dto,
            HttpSession session) {

        Integer libraryId = (Integer) session.getAttribute("libraryId");

        return ResponseEntity.ok(
                membershipService.createMembership(dto, libraryId)
        );
    }

    @GetMapping
    public ResponseEntity<Page<LibraryMembershipResponseDto>> getAllMemberships(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {

        Integer libraryId = (Integer) session.getAttribute("libraryId");

        return ResponseEntity.ok(
                membershipService.getMemberships(libraryId, page, size)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<Page<LibraryMembershipResponseDto>> searchMemberships(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {

        Integer libraryId = (Integer) session.getAttribute("libraryId");

        return ResponseEntity.ok(
                membershipService.searchMemberships(libraryId, query, page, size)
        );
    }

    @PatchMapping("/{membershipId}/payment")
    public ResponseEntity<LibraryMembershipResponseDto> payMembershipDue(

            @PathVariable Integer membershipId,

            @RequestBody MembershipPaymentRequestDto request,

            HttpSession session) {

        Integer libraryId =
                (Integer) session.getAttribute("libraryId");

        return ResponseEntity.ok(

                membershipService.payMembershipDue(
                        membershipId,
                        request,
                        libraryId
                )

        );
    }



}