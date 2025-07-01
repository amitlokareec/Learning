package com.amit.learning.akka.part2.generic_response_wrapper;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.AskPattern;
import akka.pattern.StatusReply;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

public class App {
    public static void main(String[] args) {
        ActorSystem<PaymentRequest> system = ActorSystem.create(PaymentActor.create(), "PaymentSystem");

        Duration timeout = Duration.ofSeconds(3);

        CompletionStage<StatusReply<PaymentResponse>> result =
                AskPattern.ask(system,
                        (ActorRef<StatusReply<PaymentResponse>> replyTo) ->
                                new PaymentRequest("ACC123", replyTo),
                        timeout,
                        system.scheduler()
                );

        result.whenComplete((response, throwable) -> {
            if (throwable != null) {
                System.out.println("Request failed: " + throwable.getMessage());
            } else if (response.isSuccess()) {
                System.out.println("Got success: " + response.getValue());
            } else {
                System.out.println("Got error: " + response.getError().toString());
            }
            system.terminate();
        });
    }
}
