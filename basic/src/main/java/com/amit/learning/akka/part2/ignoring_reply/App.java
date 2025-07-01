package com.amit.learning.akka.part2.ignoring_reply;

import akka.actor.typed.ActorSystem;

public class App {
    public static void main(String[] args) {
        ActorSystem<SomeRequest> actorSystem = ActorSystem.create(SomeActor.create(), "MySystem");
        actorSystem.tell(new SomeRequest("Do your thing", actorSystem.ignoreRef()));
    }
}
