package com.amit.learning.akka.part2.fsm;

import akka.actor.typed.ActorSystem;

public class MainApp {
    public static void main(String[] args) throws InterruptedException {
        // Create the root ActorSystem with initial state as idle
        ActorSystem<Command> system =
                ActorSystem.create(new PaymentAggregator().idle(), "PaymentSystem");

        // Simulate sending payments
        system.tell(new Payment("p1", 100.0));
        system.tell(new Payment("p2", 250.0));

        // Simulate a manual flush (you could also wait for a timeout)
        Thread.sleep(2000);
        system.tell(new Flush());

        // Optional: terminate the system after some time
        Thread.sleep(3000);
        system.terminate();
    }
}
