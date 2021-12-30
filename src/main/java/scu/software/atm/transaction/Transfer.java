package scu.software.atm.transaction;

import scu.software.atm.ATM;
import scu.software.atm.Session;
import scu.software.atm.physical.CustomerConsole;
import scu.software.banking.*;

public class Transfer extends Transaction {
    private int from;
    private int to;
    private Money amount;

    public Transfer(ATM atm, Session session, Card card, int pin) {
        super(atm, session, card, pin);
    }

    protected Message getSpecificsFromCustomer() throws CustomerConsole.Cancelled {
        this.from = this.atm.getCustomerConsole().readMenuChoice("Account to transfer from", AccountInformation.ACCOUNT_NAMES);
        this.to = this.atm.getCustomerConsole().readMenuChoice("Account to transfer to", AccountInformation.ACCOUNT_NAMES);
        this.amount = this.atm.getCustomerConsole().readAmount("Amount to transfer");
        this.amount.subtract(new Money(0, 50));
        return new Message(3, this.card, this.pin, this.serialNumber, this.from, this.to, this.amount);
    }

    protected Receipt completeTransaction() {
        return new Receipt(this.atm, this.card, this, this.balances) {
            {
                this.detailsPortion = new String[2];
                this.detailsPortion[0] = "TRANSFER FROM: " + AccountInformation.ACCOUNT_ABBREVIATIONS[Transfer.this.to] + " TO: " + AccountInformation.ACCOUNT_ABBREVIATIONS[Transfer.this.from];
                this.detailsPortion[1] = "AMOUNT: " + Transfer.this.amount.toString();
            }
        };
    }
}

