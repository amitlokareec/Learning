package com.amit.learning.akka.part2.generic_response_wrapper;

public class PaymentResponse {
    public final boolean success;
    public final String message;

    public PaymentResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
