package com.amit.learning.akka.part2.ignoring_reply;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class SomeActor extends AbstractBehavior<SomeRequest> {

    private SomeActor(ActorContext<SomeRequest> context) {
        super(context);
    }

    public static Behavior<SomeRequest> create() {
        return Behaviors.setup(SomeActor::new);
    }

    @Override
    public Receive<SomeRequest> createReceive() {
        return newReceiveBuilder()
                .onMessage(SomeRequest.class, this::onSomeRequest)
                .build();
    }

    private Behavior<SomeRequest> onSomeRequest(SomeRequest msg) {
        getContext().getLog().info("Received message: {}", msg.message);
        // Do something...
        return this;
    }
}

