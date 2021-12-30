package scu.software.atm.physical;

import scu.software.atm.ATM;
import scu.software.banking.Card;
import scu.software.simulation.Simulation;

public class CardReader {
    private ATM atm;

    public CardReader(ATM atm) {
        this.atm = atm;
    }

    public Card readCard() {
        return Simulation.getInstance().readCard();
    }

    public void ejectCard() {
        Simulation.getInstance().ejectCard();
    }

    public void retainCard() {
        Simulation.getInstance().retainCard();
    }
}

