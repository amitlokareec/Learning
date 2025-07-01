package com.amit.learning.akka.part2.request_response_with_ask_from_outside_actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.AskPattern;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

public class Guardian extends AbstractBehavior<Void> {

    private Guardian(ActorContext<Void> context) {
        super(context);
        runAskExample(context);
    }

    public static Behavior<Void> create() {
        return Behaviors.setup(Guardian::new);
    }

    private void runAskExample(ActorContext<Void> context) {
        ActorRef<PaymentStatusRequest> statusActor =
                context.spawn(PaymentStatusActor.create(), "statusActor");

        Duration timeout = Duration.ofMillis(3000);

        CompletionStage<PaymentStatusResponse> statusResponse =
                AskPattern.<PaymentStatusRequest, PaymentStatusResponse>ask(
                        statusActor,
                        (ActorRef<PaymentStatusResponse> replyTo) -> new PaymentStatusRequest("TXN123", replyTo),
                        timeout,
                        context.getSystem().scheduler()
                );

        statusResponse.thenAccept(response ->
                System.out.println("Received response: Approved = " + response.isApproved + ", Reason = " + response.reason)
        );
    }

    @Override
    public Receive<Void> createReceive() {
        return newReceiveBuilder().build();
    }
}
