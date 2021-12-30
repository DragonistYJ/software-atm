package scu.software.atm.physical;

import scu.software.atm.ATM;
import scu.software.banking.Money;
import scu.software.simulation.Simulation;

public class OperatorPanel {
    private ATM atm;

    public OperatorPanel(ATM atm) {
        this.atm = atm;
    }

    public Money getInitialCash() {
        return Simulation.getInstance().getInitialCash();
    }
}
