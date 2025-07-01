package com.amit.learning.akka.part2.timer.sch;

public class StartPayment implements PaymentCommand {
    public final String paymentId;

    public StartPayment(String paymentId) {
        this.paymentId = paymentId;
    }
}
