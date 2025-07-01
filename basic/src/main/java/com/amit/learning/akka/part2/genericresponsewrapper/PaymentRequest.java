package com.amit.learning.akka.part2.genericresponsewrapper;

import akka.actor.typed.ActorRef;
import akka.pattern.StatusReply;

public class PaymentRequest {
    public final String accountId;
    public final ActorRef<StatusReply<PaymentResponse>> replyTo;

    public PaymentRequest(String accountId, ActorRef<StatusReply<PaymentResponse>> replyTo) {
        this.accountId = accountId;
        this.replyTo = replyTo;
    }
}