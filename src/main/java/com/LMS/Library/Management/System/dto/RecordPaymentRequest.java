package com.LMS.Library.Management.System.dto;

// dto/RecordPaymentRequest.java
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RecordPaymentRequest {

    @Positive(message = "Payment amount must be positive")
    private double amount;

    private String note;    // optional note, e.g. "cash payment", "UPI"
}