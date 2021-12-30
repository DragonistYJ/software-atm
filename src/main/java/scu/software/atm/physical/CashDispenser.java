package scu.software.atm.physical;

import scu.software.banking.Money;
import scu.software.simulation.Simulation;

public class CashDispenser {
    private Log log;
    private Money cashOnHand;

    public CashDispenser(Log log) {
        this.log = log;
        this.cashOnHand = new Money(0);
    }

    public void setInitialCash(Money initialCash) {
        this.cashOnHand = initialCash;
    }

    public boolean checkCashOnHand(Money amount) {
        return amount.lessEqual(this.cashOnHand);
    }

    public void dispenseCash(Money amount) {
        this.cashOnHand.subtract(amount);
        Simulation.getInstance().dispenseCash(amount);
        this.log.logCashDispensed(amount);
    }
}

