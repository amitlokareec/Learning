package com.amit.learning.akka.part2.genericresponsewrapper;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.pattern.StatusReply;

public class PaymentActor extends AbstractBehavior<PaymentRequest> {

    public PaymentActor(ActorContext<PaymentRequest> context) {
        super(context);
    }

    public static Behavior<PaymentRequest> create() {
        return Behaviors.setup(PaymentActor::new);
    }

    @Override
    public Receive<PaymentRequest> createReceive() {
        return newReceiveBuilder()
                .onMessage(PaymentRequest.class, this::onPaymentRequest)
                .build();
    }

    private Behavior<PaymentRequest> onPaymentRequest(PaymentRequest request) {
        if (isValidAccount(request.accountId)) {
            request.replyTo.tell(StatusReply.success(
                    new PaymentResponse(true, "Payment successful")));
        } else {
            request.replyTo.tell(StatusReply.error(
                    new PaymentResponse(false, "Invalid account").toString()));
        }
        return this;
    }

    private boolean isValidAccount(String accountId) {
        return accountId != null && accountId.startsWith("ACC");
    }

}
