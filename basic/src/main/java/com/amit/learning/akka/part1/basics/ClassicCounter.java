package com.amit.learning.akka.part1.basics;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * In traditional Akka (classic actors), state is stored in mutable instance variables. This can cause race conditions if an actor accidentally exposes state outside.
 */
public class ClassicCounter extends AbstractActor {
    private int count = 0; // A Mutable state. If another thread somehow accesses this, it could lead to inconsistent behavior.

    public static Props props() {
        return Props.create(ClassicCounter.class);
    }

    public static void main(String[] args) {
        // Create the actor system
        ActorSystem system = ActorSystem.create("CounterSystem");

        // Create the actor
        ActorRef counter = system.actorOf(ClassicCounter.props(), "classicCounter");

        // Send messages to the actor
        counter.tell(1, ActorRef.noSender());
        counter.tell(2, ActorRef.noSender());
        counter.tell(5, ActorRef.noSender());

        // Gracefully shutdown the system after a delay (to let messages process)
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        system.terminate();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Integer.class, msg -> {
                    count++; // Modifies mutable state
                    System.out.println("Current Count: " + count);
                })
                .build();
    }
}
