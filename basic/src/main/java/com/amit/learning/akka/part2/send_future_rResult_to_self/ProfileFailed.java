package com.amit.learning.akka.part2.send_future_rResult_to_self;

public class ProfileFailed implements Command {
    public final Throwable cause;

    public ProfileFailed(Throwable cause) {
        this.cause = cause;
    }
}
