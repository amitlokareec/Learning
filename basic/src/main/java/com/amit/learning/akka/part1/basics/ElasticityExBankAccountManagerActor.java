package com.amit.learning.akka.part1.basics;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.ArrayList;
import java.util.List;

import static com.amit.learning.akka.part1.basics.ImmutableMessageWithBankAccountEx.Deposit;

public class ElasticityExBankAccountManagerActor extends AbstractBehavior<Deposit> {
    private final List<ActorRef<Deposit>> workers = new ArrayList<>();
    private int taskQueueSize = 0;

    public ElasticityExBankAccountManagerActor(ActorContext<Deposit> context) {
        super(context);
    }

    public static Behavior<Deposit> create() {
        return Behaviors.setup(ElasticityExBankAccountManagerActor::new);
    }

    public static void main(String[] args) throws InterruptedException {
        // Create the root actor system with BankAccountManagerActor
        ActorSystem<Deposit> system =
                ActorSystem.create(ElasticityExBankAccountManagerActor.create(), "BankSystem");

        // Send deposit messages
        system.tell(new Deposit(100));
        system.tell(new Deposit(200));
        system.tell(new Deposit(300));
        system.tell(new Deposit(400));
        system.tell(new Deposit(500));

        // Optional: give some time for actors to process before the program ends
        Thread.sleep(2000);

        // Shutdown
        system.terminate();
    }

    @Override
    public Receive<Deposit> createReceive() {
        return newReceiveBuilder()
                .onMessage(Deposit.class, this::onDeposit)
                .build();
    }

    private Behavior<Deposit> onDeposit(Deposit deposit) {
        taskQueueSize++;
        getContext().getLog().info("Task received: " + deposit.amount +
                ". Queue size: " + taskQueueSize);

        if (taskQueueSize > workers.size()) {
            ActorRef<Deposit> newWorker = getContext().spawn(ElasticityExBankAccountWorkerActor.create(), "worker-" + workers.size());
            workers.add(newWorker);
            getContext().getLog().info("Spawning new worker: " + newWorker.path().name());
        } else if (taskQueueSize < workers.size() / 2) {
            ActorRef<Deposit> workerToStop = workers.remove(workers.size() - 1);
            getContext().getLog().info(
                    "Removing a worker: " + workerToStop.path().name());
            getContext().stop(workerToStop);
        }


        // Assign task to one of the available workers
        ActorRef<Deposit> worker = workers.get(taskQueueSize % workers.size());
        worker.tell(deposit);

        return this;
    }

}