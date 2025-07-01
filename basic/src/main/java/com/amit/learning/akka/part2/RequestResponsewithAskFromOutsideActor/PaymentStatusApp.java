package com.amit.learning.akka.part2.RequestResponsewithAskFromOutsideActor;

import akka.actor.typed.ActorSystem;

public class PaymentStatusApp {

    public static void main(String[] args) {
        ActorSystem<Void> system = ActorSystem.create(Guardian.create(), "PaymentSystem");
    }
}
