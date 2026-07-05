package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dto.CreateMembershipRequest;
import com.LMS.Library.Management.System.dto.LibraryMembershipResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public interface MembershipService {

    LibraryMembershipResponseDto createMembership(
            CreateMembershipRequest dto,
            Integer libraryId);

//    Page<MembershipResponse> getMemberships(
//            Integer libraryId,
//            int page,
//            int size);
//
//    Page<MembershipResponse> searchMemberships(
//            Integer libraryId,
//            String keyword,
//            int page,
//            int size);

}

 