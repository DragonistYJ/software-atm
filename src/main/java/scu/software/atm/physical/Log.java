package scu.software.atm.physical;

import scu.software.banking.Message;
import scu.software.banking.Money;
import scu.software.banking.Status;
import scu.software.simulation.Simulation;

public class Log {
    public Log() {
    }

    public void logSend(Message message) {
        Simulation.getInstance().printLogLine("Message:   " + message.toString());
    }

    public void logResponse(Status response) {
        Simulation.getInstance().printLogLine("Response:  " + response.toString());
    }

    public void logCashDispensed(Money amount) {
        Simulation.getInstance().printLogLine("Dispensed: " + amount.toString());
    }

    public void logEnvelopeAccepted() {
        Simulation.getInstance().printLogLine("Envelope:  received");
    }
}

