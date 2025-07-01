package com.amit.learning.akka.part2.fsm;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;

import java.util.ArrayList;
import java.util.List;

public class PaymentAggregator {

    // Flush payments to the core banking system
    private static void flushToCoreBanking(List<Payment> payments) {
        // Imagine this sends payments to a remote system...
        payments.forEach(payment -> System.out.println("Flushing payment: " + payment.id()));
    }

    public Behavior<Command> buffering(List<Payment> payments) {
        /*The withTimers block provides access to a TimerScheduler,
           which is used to set up timers for that actor.
           In this case, the actor is buffering payment events and flush them
           to a core banking system either when a Flush command is received
           or after a timeout(here it is Flush command).
      */
        return Behaviors.withTimers(timers ->
                Behaviors.receive(Command.class)
                        .onMessage(Payment.class, payment -> {

                            // Add payment to the buffer
                            var updated = new ArrayList<>(payments);
                            updated.add(payment);
                            // Stay in the buffering state
                            return buffering(updated);
                        })
                        .onMessage(Flush.class, flush -> {
                            // Handle flush command, process payments
                            flushToCoreBanking(payments);
                            return idle();// After flushing, go to idle state
                        })
                        .onSignal(Timeout.class, sig -> {
                            // Handle timeout signal
                            flushToCoreBanking(payments);
                            return idle();
                        })
                        .build());
    }

    public Behavior<Command> idle() {
        return Behaviors.receive(Command.class)
                .onMessage(Payment.class, payment -> {
                    // Start buffering when the first payment comes in
                    List<Payment> initial = new ArrayList<>();
                    initial.add(payment);
                    return buffering(initial);
                })
                .build();
    }
}