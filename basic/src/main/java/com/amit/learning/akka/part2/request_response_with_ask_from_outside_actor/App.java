package com.amit.learning.akka.part2.request_response_with_ask_from_outside_actor;

import akka.actor.typed.ActorSystem;

public class App {

    public static void main(String[] args) {
        ActorSystem<Void> system = ActorSystem.create(Guardian.create(), "PaymentSystem");
    }
}
