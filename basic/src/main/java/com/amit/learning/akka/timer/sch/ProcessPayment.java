package com.amit.learning.akka.timer.sch;

public class ProcessPayment implements ManagerCommand {
    public final String paymentId;

    public ProcessPayment(String paymentId) {
        this.paymentId = paymentId;
    }
}
