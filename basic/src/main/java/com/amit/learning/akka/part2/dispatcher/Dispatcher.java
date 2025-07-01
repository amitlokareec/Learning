package com.amit.learning.akka.part2.dispatcher;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.DispatcherSelector;
import akka.actor.typed.javadsl.Behaviors;

public class Dispatcher {

    public static void main(String[] args) {
        ActorSystem<String> system = ActorSystem.create(Main.create(), "DispatcherSystem");
        system.tell("start");
    }

    // Main actor that spawns a child with custom dispatcher
    public static class Main {
        public static Behavior<String> create() {
            return Behaviors.setup(context -> {

                // Use dispatcher from config
                DispatcherSelector dispatcherSelector = DispatcherSelector.fromConfig("akka.actor.my-dispatcher");

                ActorRef<String> worker = context.spawn(
                        Worker.create(),
                        "my-worker",//written in application.conf
                        dispatcherSelector
                );

                return Behaviors.receiveMessage(msg -> {
                    if (msg.equals("start")) {
                        worker.tell("hello Amit!!");
                    }
                    return Behaviors.same();
                });
            });
        }
    }

    // Worker actor that runs on custom dispatcher
    public static class Worker {
        public static Behavior<String> create() {
            return Behaviors.receive((context, message) -> {
                context.getLog().info("Worker received: {}", message);
                return Behaviors.same();
            });
        }
    }
}

