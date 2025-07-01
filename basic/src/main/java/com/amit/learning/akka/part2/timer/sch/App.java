package com.amit.learning.akka.part2.timer.sch;

import akka.actor.typed.ActorSystem;

public class App {
    public static void main(String[] args) {
        ActorSystem<ManagerCommand> system =
                ActorSystem.create(PaymentManager.create(), "PaymentSystem");

        // Send message to the root actor
        system.tell(new ProcessPayment("TXN001"));
        system.tell(new ProcessPayment("TXN002"));

        // Wait for a short time to allow message processing
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        system.terminate();
    }
}
