package com.amit.learning.akka.part2.RequestResponsewithAskFromOutsideActor;

import akka.actor.typed.ActorRef;

public class PaymentStatusRequest {
    public final String paymentId;
    public final ActorRef<PaymentStatusResponse> replyTo;

    public PaymentStatusRequest(String paymentId, ActorRef<PaymentStatusResponse> replyTo) {
        this.paymentId = paymentId;
        this.replyTo = replyTo;
    }
}
