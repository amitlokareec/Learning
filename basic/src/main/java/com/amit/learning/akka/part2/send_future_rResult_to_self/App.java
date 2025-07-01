package com.amit.learning.akka.part2.send_future_rResult_to_self;

import akka.actor.typed.ActorSystem;

public class App {
    public static void main(String[] args) {
        ActorSystem<Command> system = ActorSystem.create(ProfileActor.create(), "UserProfileSystem");
        system.tell(new GetProfile("u123"));
    }
}
