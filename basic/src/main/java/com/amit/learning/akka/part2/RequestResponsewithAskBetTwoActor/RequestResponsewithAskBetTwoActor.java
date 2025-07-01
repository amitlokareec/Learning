package com.amit.learning.akka.part2.RequestResponsewithAskBetTwoActor;

import akka.actor.typed.ActorRef;

public class RequestResponsewithAskBetTwoActor {

    class PaymentStatusRequest implements Command {
        public final String txnId;
        public final ActorRef<PaymentStatusResponse> replyTo;

        public PaymentStatusRequest(String txnId, ActorRef<PaymentStatusResponse> replyTo) {
            this.txnId = txnId;
            this.replyTo = replyTo;
        }
    }

    class PaymentStatusResponse {
        public final boolean isApproved;
        public final String reason;

        public PaymentStatusResponse(boolean isApproved, String reason) {
            this.isApproved = isApproved;
            this.reason = reason;
        }
    }
}
