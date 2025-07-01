package com.amit.learning.akka.part2.request_response_with_ask_from_outside_actor;

public class PaymentStatusResponse {
    public final boolean isApproved;
    public final String reason;

    public PaymentStatusResponse(boolean isApproved, String reason) {
        this.isApproved = isApproved;
        this.reason = reason;
    }
}
