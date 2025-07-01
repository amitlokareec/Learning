package com.amit.learning.akka.part1.basics;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;

public class Scalability {
    public static Behavior<Object> createChildActors() {
        return Behaviors.setup(context -> {
            // Spawning means creating child actors
            ActorRef<Object> actor1 = context.spawn(ImmutableMessageWithBankAccountEx.create(), "actor1");
            ActorRef<Object> actor2 = context.spawn(ImmutableMessageWithBankAccountEx.create(), "actor2");

            //message is sent
            actor1.tell(new ImmutableMessageWithBankAccountEx.Deposit(100));
            actor2.tell(new ImmutableMessageWithBankAccountEx.Deposit(200));

            return Behaviors.empty();
        });
    }

    public static void main(String[] args) {
        // Create the top-level ActorSystem
        ActorSystem<Object> system = ActorSystem.create(
                Scalability.createChildActors(), "ParentSystem"
        );

        // Wait a bit to allow messages to be processed
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        system.terminate();
    }
}
