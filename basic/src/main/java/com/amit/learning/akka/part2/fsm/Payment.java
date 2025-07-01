package com.amit.learning.akka.part2.fsm;

public record Payment(String id, double amount) implements Command {
}
