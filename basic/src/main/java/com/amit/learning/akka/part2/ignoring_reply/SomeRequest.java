package com.amit.learning.akka.part2.ignoring_reply;

import akka.Done;
import akka.actor.typed.ActorRef;

public class SomeRequest {
    public final String message;
    public final ActorRef<Done> replyTo;

    public SomeRequest(String message, ActorRef<Done> replyTo) {
        this.message = message;
        this.replyTo = replyTo;
    }
}
