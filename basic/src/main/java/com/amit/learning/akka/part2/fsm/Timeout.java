package com.amit.learning.akka.part2.fsm;

import akka.actor.typed.Signal;

public record Timeout() implements Signal {//Note timeout is implementing signal
}

