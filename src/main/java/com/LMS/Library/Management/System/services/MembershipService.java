package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dto.CreateMembershipRequest;
import com.LMS.Library.Management.System.dto.LibraryMembershipResponseDto;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface MembershipService {

    LibraryMembershipResponseDto createMembership(
            CreateMembershipRequest dto,
            Integer libraryId);


    Page<LibraryMembershipResponseDto> getMemberships(
            Integer libraryId,
            int page,
            int size);
//
    Page<LibraryMembershipResponseDto> searchMemberships(
            Integer libraryId,
            String keyword,
            int page,
            int size);

}

 