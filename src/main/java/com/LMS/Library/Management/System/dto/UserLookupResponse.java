package com.LMS.Library.Management.System.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLookupResponse {
    private Integer userId;
    private String name;
    private String email;
    private String role;
    // tells the library if this user already has an active membership
    private boolean hasActiveMembership;
    private String existingMembershipId;  // null if none
}
