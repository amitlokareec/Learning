package com.amit.learning.akka.part2.send_future_rResult_to_self;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

class UserService {
    public CompletionStage<UserProfile> fetchProfile(String userId) {
        // Simulate async fetch (normally you'd call a DB or API)
        return CompletableFuture.supplyAsync(() -> new UserProfile(userId, "Amit"));
    }
}
