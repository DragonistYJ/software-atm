package scu.software.atm.transaction;

import scu.software.atm.ATM;
import scu.software.atm.Session;
import scu.software.atm.physical.CustomerConsole;
import scu.software.banking.*;

public class Deposit extends Transaction {
    private int to;
    private Money amount;

    public Deposit(ATM atm, Session session, Card card, int pin) {
        super(atm, session, card, pin);
    }

    protected Message getSpecificsFromCustomer() throws CustomerConsole.Cancelled {
        // 账户的下标
        this.to = this.atm.getCustomerConsole().readMenuChoice("Account to deposit to", AccountInformation.ACCOUNT_NAMES);
        // 用户输入的汇款金额
        this.amount = this.atm.getCustomerConsole().readAmount("Amount to deposit");
        return new Message(1, this.card, this.pin, this.serialNumber, -1, this.to, this.amount);
    }

    protected Receipt completeTransaction() throws CustomerConsole.Cancelled {
        this.atm.getEnvelopeAcceptor().acceptEnvelope();
        Status status = this.atm.getNetworkToBank().sendMessage(new Message(2, this.card, this.pin, this.serialNumber, -1, this.to, this.amount), this.balances);
        return new Receipt(this.atm, this.card, this, this.balances) {
            {
                this.detailsPortion = new String[2];
                this.detailsPortion[0] = "DEPOSIT TO: " + AccountInformation.ACCOUNT_ABBREVIATIONS[Deposit.this.to];
                this.detailsPortion[1] = "AMOUNT: " + Deposit.this.amount.toString();
            }
        };
    }
}

