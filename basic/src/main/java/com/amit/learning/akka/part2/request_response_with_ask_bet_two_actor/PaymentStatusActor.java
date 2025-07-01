package com.amit.learning.akka.part2.request_response_with_ask_bet_two_actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.AskPattern;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.amit.learning.akka.part2.request_response_with_ask_bet_two_actor.RequestResponsewithAskBetTwoActor.PaymentStatusRequest;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

public class PaymentStatusActor extends AbstractBehavior<Command> {

    private final ActorRef<PaymentStatusRequest> selfRef;

    private PaymentStatusActor(ActorContext<Command> context) {
        super(context);
        this.selfRef = context.messageAdapter(RequestResponsewithAskBetTwoActor.PaymentStatusRequest.class, response ->
                new Command() {
                } // We don't use the response internally here, just logging externally.
        );
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(PaymentStatusActor::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(PaymentStatusRequest.class, this::onPaymentStatusRequest)
                .onMessage(TriggerAsk.class, this::onTriggerAsk)
                .build();
    }

    private Behavior<Command> onPaymentStatusRequest(PaymentStatusRequest msg) {
        getContext().getLog().info("Received request for txnId: {}", msg.txnId);
        msg.replyTo.tell(new RequestResponsewithAskBetTwoActor().new PaymentStatusResponse(true, "Approved"));
        return this;
    }

    private Behavior<Command> onTriggerAsk(TriggerAsk msg) {
        ActorRef<PaymentStatusRequest> statusActor = getContext().getSelf().narrow();

        Duration timeout = Duration.ofSeconds(3);

        CompletionStage<RequestResponsewithAskBetTwoActor.PaymentStatusResponse> response =
                AskPattern.<PaymentStatusRequest, RequestResponsewithAskBetTwoActor.PaymentStatusResponse>ask(
                        statusActor,
                        replyTo -> new RequestResponsewithAskBetTwoActor().new PaymentStatusRequest("TXN123", replyTo),
                        timeout,
                        getContext().getSystem().scheduler()
                );

        response.thenAccept(r ->
                getContext().getLog().info("Ask got response: {} - {}", r.isApproved, r.reason)
        );

        return this;
    }
}
