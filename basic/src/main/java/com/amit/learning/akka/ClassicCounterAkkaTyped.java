package com.amit.learning.akka;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;

public class ClassicCounterAkkaTyped {
    public static Behavior<Integer> create() {
        return behavior(0);
    }
    private static Behavior<Integer> behavior(int count) {
        return Behaviors.receive((context, message) -> {
            int newCount = count + message;
            System.out.println("Received: " + message + " | Count: " + newCount);
            return behavior(newCount);
        });
    }

    public static void main(String[] args) {
        ActorSystem<Integer> counterSystem = ActorSystem.create(create(), "TypedCounterSystem");

        counterSystem.tell(1);
        counterSystem.tell(2);
        counterSystem.tell(5);

        try {
            Thread.sleep(1000); // Let it process before termination
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        counterSystem.terminate();
    }
}
