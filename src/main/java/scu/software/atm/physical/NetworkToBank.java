package scu.software.atm.physical;

import scu.software.banking.Balances;
import scu.software.banking.Message;
import scu.software.banking.Status;
import scu.software.simulation.Simulation;

import java.net.InetAddress;

public class NetworkToBank {
    private Log log;
    private InetAddress bankAddress;

    public NetworkToBank(Log log, InetAddress bankAddress) {
        this.log = log;
        this.bankAddress = bankAddress;
    }

    public void openConnection() {
    }

    public void closeConnection() {
    }

    public Status sendMessage(Message message, Balances balances) {
        this.log.logSend(message);
        Status result = Simulation.getInstance().sendMessage(message, balances);
        this.log.logResponse(result);
        return result;
    }
}

