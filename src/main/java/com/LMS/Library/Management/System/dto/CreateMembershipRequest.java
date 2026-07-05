package com.LMS.Library.Management.System.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateMembershipRequest {

    @NotNull(message = "User email is required")
    private String userEmail;           // library enters the registered user's email

    @NotNull(message = "Plan is required")
    private Integer planId;        // BASIC | STANDARD | PREMIUM

    @PositiveOrZero(message = "Amount paid cannot be negative")
    private double amountPaid;          // how much the member paid right now (can be 0 or partial)
}
