package com.amit.learning.akka.part1.immutability;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class ImmutableMessageWithBankAccountEx {

    public static void main(String[] args) {
        // Create the BankAccountActor system
        ActorSystem<Object> bankAccount = ActorSystem.create(
                ImmutableMessageWithBankAccountEx.create(),
                "BankAccountSystem"
        );

        // Deposit money
        bankAccount.tell(new ImmutableMessageWithBankAccountEx.Deposit(100));
        bankAccount.tell(new ImmutableMessageWithBankAccountEx.Deposit(250));

        // Define an inline actor to receive the balance response
        ActorRef<Integer> replyReceiver = ActorSystem.create(
                Behaviors.receive(Integer.class)
                        .onMessage(Integer.class, balance -> {
                            System.out.println("Received balance: " + balance);
                            return Behaviors.stopped();
                        })
                        .build(),
                "BalanceReceiver"
        );

        // Ask for balance
        bankAccount.tell(new ImmutableMessageWithBankAccountEx.GetBalance(replyReceiver));

        // Delay termination to allow message processing
        try {
            Thread.sleep(2000); // Wait for balance to be printed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        bankAccount.terminate();
    }

    /**
     * Init behavior
     */
    public static Behavior<Object> create() {
        return Behaviors.setup(BankAccountActor::new);
    }

    public static class Deposit {
        public final int amount;

        public Deposit(int amount) {
            this.amount = amount;
        }
    }

    public static class GetBalance {
        public final ActorRef<Integer> replyTo;

        public GetBalance(ActorRef<Integer> replyTo) {
            this.replyTo = replyTo;
        }
    }

    // Actor implementation
    public static class BankAccountActor extends AbstractBehavior<Object> {

        private int balance = 0;

        public BankAccountActor(ActorContext<Object> context) {
            super(context);
        }

        @Override
        public Receive<Object> createReceive() {
            return newReceiveBuilder()
                    .onMessage(Deposit.class, this::onDeposit)
                    .onMessage(GetBalance.class, this::onGetBalance)
                    .build();
        }

        private Behavior<Object> onDeposit(Deposit deposit) {
            System.out.println(deposit.amount + " Deposited by " + getContext().getSelf().path().name());
            balance += deposit.amount;
            return this;
        }

        private Behavior<Object> onGetBalance(GetBalance getBalance) {
            getBalance.replyTo.tell(balance);
            return this;
        }
    }
}
