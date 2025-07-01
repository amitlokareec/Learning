package com.amit.learning.akka.part2.request_response_with_ask_from_outside_actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;

public class PaymentStatusActor {
    public static Behavior<PaymentStatusRequest> create() {
        return Behaviors.receive(PaymentStatusRequest.class)
                .onMessage(PaymentStatusRequest.class, PaymentStatusActor::onStatusRequest)
                .build();
    }

    private static Behavior<PaymentStatusRequest> onStatusRequest(PaymentStatusRequest msg) {
        // Simple business logic: approve if paymentId starts with TXN
        boolean approved = msg.paymentId.startsWith("TXN");
        String reason = approved ? "Approved" : "Rejected: Invalid ID";
        msg.replyTo.tell(new PaymentStatusResponse(approved, reason));
        return Behaviors.same();
    }
}
