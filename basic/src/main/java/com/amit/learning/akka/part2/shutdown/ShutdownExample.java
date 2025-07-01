package com.amit.learning.akka.part2.shutdown;

import akka.Done;
import akka.actor.CoordinatedShutdown;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.Behaviors;

import java.util.concurrent.CompletableFuture;

public class ShutdownExample {

    public static void main(String[] args) {
        // Create the ActorSystem
        ActorSystem<String> system = ActorSystem.create(Behaviors.empty(), "ShutdownSystem");

        // Register shutdown hook
        CoordinatedShutdown.get(system)
                .addTask(
                        CoordinatedShutdown.PhaseBeforeServiceUnbind(),  // Choose the phase
                        "close-payments",                                // Name of the task
                        () -> {
                            // Your graceful shutdown logic goes here
                            System.out.println("Closing payments gracefully...");

                            // Simulate some cleanup or say somthing like DB closing logic
                            return CompletableFuture.completedFuture(Done.getInstance());
                        });

        // Schedule shutdown after 10 seconds just for demo
        system.scheduler().scheduleOnce(
                scala.concurrent.duration.Duration.create(10, "seconds"),
                () -> {
                    System.out.println("Initiating coordinated shutdown...");
                    CoordinatedShutdown.get(system).run(CoordinatedShutdown.unknownReason());
                },
                system.executionContext()
        );
    }
}

