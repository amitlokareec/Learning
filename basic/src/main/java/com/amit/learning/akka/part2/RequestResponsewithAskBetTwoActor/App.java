package com.amit.learning.akka.part2.RequestResponsewithAskBetTwoActor;

import akka.actor.typed.ActorSystem;

public class App {
    public static void main(String[] args) {
        ActorSystem<Command> system = ActorSystem.create(PaymentStatusActor.create(), "PaymentSystem");
        system.tell(new TriggerAsk());
    }
}
