package com.amit.learning.akka.part2.request_response_with_ask_bet_two_actor;

import akka.actor.typed.ActorSystem;

public class App {
    public static void main(String[] args) {
        ActorSystem<Command> system = ActorSystem.create(PaymentStatusActor.create(), "PaymentSystem");
        system.tell(new TriggerAsk());
    }
}
