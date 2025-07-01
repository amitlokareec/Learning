package com.amit.learning.akka.part2.send_future_rResult_to_self;

class WrappedProfile implements Command {
    public final UserProfile profile;

    public WrappedProfile(UserProfile profile) {
        this.profile = profile;
    }
}
