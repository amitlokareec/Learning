package com.amit.learning.akka.part2.timer.sch;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class PaymentManager extends AbstractBehavior<ManagerCommand> {
    private final ActorContext<ManagerCommand> context;

    /**
     * During the actor creation process, the ActorContext<T>
     * is automatically injected by Akka and passed into the constructor
     * of the actor. During the actor creation process,
     * the ActorContext<T> is automatically injected by Akka and
     * passed into the constructor of the actor.
     */
    public PaymentManager(ActorContext<ManagerCommand> context) {
        super(context);
        this.context = context;//setting up here to be used acorss the class
    }

    public static Behavior<ManagerCommand> create() {
        /*
          ActorContext is automatically injected by the Akka actor system
          when an actor is created using Behaviors.setup()....
          When calling Behaviors.setup, Akka sets up the actor and invokes the
          constructor of the actor (e.g., PaymentManager) to initialize it.
        */
        return Behaviors.setup(PaymentManager::new);
    }

    @Override
    public Receive<ManagerCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(ProcessPayment.class, this::onProcessPayment)
                .onSignal(Terminated.class, this::onChildTerminated)
                .build();
    }

    private Behavior<ManagerCommand> onProcessPayment(ProcessPayment msg) {
        ActorRef<PaymentCommand> worker =
                context.spawn(PaymentWorker.create(),
                        "payment-" + msg.paymentId);
        context.watch(worker);
        //Spawed actor is told to work on the StartPayment Object
        worker.tell(new StartPayment(msg.paymentId));
        return this;
    }

    private Behavior<ManagerCommand> onChildTerminated(Terminated signal) {
        context.getLog().info("Child {} stopped", signal.getRef().path().name());
        return this;
    }
}
