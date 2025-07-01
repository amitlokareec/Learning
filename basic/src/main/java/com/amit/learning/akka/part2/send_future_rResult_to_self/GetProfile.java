package com.amit.learning.akka.part2.send_future_rResult_to_self;

class GetProfile implements Command {
    public final String userId;

    public GetProfile(String userId) {
        this.userId = userId;
    }
}
