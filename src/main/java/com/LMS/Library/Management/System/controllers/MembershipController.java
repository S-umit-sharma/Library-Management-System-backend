package com.LMS.Library.Management.System.controllers;


import com.LMS.Library.Management.System.dto.CreateMembershipRequest;
import com.LMS.Library.Management.System.dto.LibraryMembershipResponseDto;
import com.LMS.Library.Management.System.services.MembershipService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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

//    @GetMapping
//    public ResponseEntity<Page<MembershipResponse>> getAllMemberships(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            HttpSession session) {
//
//        Integer libraryId = (Integer) session.getAttribute("libraryId");
//
//        return ResponseEntity.ok(
//                membershipService.getMemberships(libraryId, page, size)
//        );
//    }

}