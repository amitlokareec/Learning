package com.amit.learning.akka.part1.elasticity;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.amit.learning.akka.part1.immutability.ImmutableMessageWithBankAccountEx;

public class ElasticityExBankAccountWorkerActor extends AbstractBehavior<ImmutableMessageWithBankAccountEx.Deposit> {

    private int balance = 0;

    public ElasticityExBankAccountWorkerActor(ActorContext<ImmutableMessageWithBankAccountEx.Deposit> context) {
        super(context);
    }

    public static Behavior<ImmutableMessageWithBankAccountEx.Deposit> create() {
        return Behaviors.setup(ElasticityExBankAccountWorkerActor::new);
    }

    @Override
    public Receive<ImmutableMessageWithBankAccountEx.Deposit> createReceive() {
        return newReceiveBuilder()
                .onMessage(ImmutableMessageWithBankAccountEx.Deposit.class,
                        this::onDeposit)
                .build();
    }

    private Behavior<ImmutableMessageWithBankAccountEx.Deposit>
    onDeposit(ImmutableMessageWithBankAccountEx.Deposit deposit) {
        balance += deposit.amount;
        getContext().getLog().
                info("Worker {} processed deposit: {}, new balance: {}",
                        getContext().getSelf().path().name(), deposit.amount, balance);
        return this;
    }
}
