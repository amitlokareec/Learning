package com.amit.learning.akka.part2.send_future_rResult_to_self;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.concurrent.CompletionStage;

class ProfileActor extends AbstractBehavior<Command> {

    private final UserService userService;

    private ProfileActor(ActorContext<Command> context) {
        super(context);
        this.userService = new UserService();
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(ProfileActor::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(GetProfile.class, this::onGetProfile)
                .onMessage(WrappedProfile.class, this::onWrappedProfile)
                .onMessage(ProfileFailed.class, this::onProfileFailed)
                .build();
    }

    private Behavior<Command> onGetProfile(GetProfile msg) {
        CompletionStage<UserProfile> future = userService.fetchProfile(msg.userId);

        getContext().pipeToSelf(future, (ok, ex) -> {
            if (ok != null) {
                return new WrappedProfile(ok);
            } else {
                return new ProfileFailed(ex);
            }
        });

        return this;
    }

    private Behavior<Command> onWrappedProfile(WrappedProfile msg) {
        getContext().getLog().info("Profile fetched: userId={}, name={}", msg.profile.userId, msg.profile.name);
        return this;
    }

    private Behavior<Command> onProfileFailed(ProfileFailed msg) {
        getContext().getLog().error("Failed to fetch profile", msg.cause);
        return this;
    }
}

