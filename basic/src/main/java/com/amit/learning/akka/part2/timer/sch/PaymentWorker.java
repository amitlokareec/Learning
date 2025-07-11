package com.amit.learning.akka.part2.timer.sch;

import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class PaymentWorker extends AbstractBehavior<PaymentCommand> {

    private PaymentWorker(ActorContext<PaymentCommand> context) {
        super(context);
    }

    public static Behavior<PaymentCommand> create() {
        return Behaviors.setup(PaymentWorker::new);
    }

    @Override
    public Receive<PaymentCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(StartPayment.class, this::onStartPayment)
                .onSignal(Terminated.class, signal -> {
                    getContext().getLog().info("Watched actor stopped: {}", signal.getRef());
                    return Behaviors.same();
                })
                .build();
    }

    private Behavior<PaymentCommand> onStartPayment(StartPayment msg) {
        getContext().getLog().info("Processing payment: {}", msg.paymentId);
        return this;
    }
}
