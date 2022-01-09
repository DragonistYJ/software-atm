package scu.software.atm.transaction;

import scu.software.atm.ATM;
import scu.software.atm.Session;
import scu.software.atm.physical.CustomerConsole;
import scu.software.banking.*;

public class Withdrawal extends Transaction {
    private int from;
    private Money amount;

    public Withdrawal(ATM atm, Session session, Card card, int pin) {
        super(atm, session, card, pin);
    }

    protected Message getSpecificsFromCustomer() throws CustomerConsole.Cancelled {
        this.from = this.atm.getCustomerConsole().readMenuChoice("Account to withdraw from", AccountInformation.ACCOUNT_NAMES);
        String[] amountOptions = new String[]{"$20", "$40", "$60", "$100", "$200"};
        Money[] amountValues = new Money[]{new Money(20), new Money(40), new Money(60), new Money(100), new Money(200)};
        String amountMessage = "";
        boolean validAmount = false;

        while (!validAmount) {
            this.amount = amountValues[this.atm.getCustomerConsole().readMenuChoice(amountMessage + "Amount of cash to withdraw", amountOptions)];
            validAmount = this.atm.getCashDispenser().checkCashOnHand(this.amount);
            if (!validAmount) {
                amountMessage = "Insuficient cash available\n";
            }
        }

        return new Message(0, this.card, this.pin, this.serialNumber, this.from, -1, this.amount);
    }

    protected Receipt completeTransaction() {
        this.atm.getCashDispenser().dispenseCash(this.amount);
        return new Receipt(this.atm, this.card, this, this.balances) {
            {
                this.detailsPortion = new String[2];
                this.detailsPortion[0] = "WITHDRAWAL FROM: " + AccountInformation.ACCOUNT_ABBREVIATIONS[Withdrawal.this.from];
                this.detailsPortion[1] = "AMOUNT: " + Withdrawal.this.amount.toString();
            }
        };
    }
}

