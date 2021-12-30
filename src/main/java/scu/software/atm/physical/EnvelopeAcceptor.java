package scu.software.atm.physical;

import scu.software.simulation.Simulation;

public class EnvelopeAcceptor {
    private Log log;

    public EnvelopeAcceptor(Log log) {
        this.log = log;
    }

    public void acceptEnvelope() throws CustomerConsole.Cancelled {
        boolean inserted = Simulation.getInstance().acceptEnvelope();
        if (inserted) {
            this.log.logEnvelopeAccepted();
        } else {
            throw new CustomerConsole.Cancelled();
        }
    }
}

