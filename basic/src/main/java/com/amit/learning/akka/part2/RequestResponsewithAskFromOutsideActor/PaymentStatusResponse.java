package com.amit.learning.akka.part2.RequestResponsewithAskFromOutsideActor;

public class PaymentStatusResponse {
    public final boolean isApproved;
    public final String reason;

    public PaymentStatusResponse(boolean isApproved, String reason) {
        this.isApproved = isApproved;
        this.reason = reason;
    }
}
