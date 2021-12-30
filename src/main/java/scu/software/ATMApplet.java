package scu.software;

import scu.software.atm.ATM;
import scu.software.simulation.Simulation;

import java.applet.Applet;
import java.awt.Panel;
import java.net.InetAddress;

public class ATMApplet extends Applet {
    public ATMApplet() {
    }

    public void init() {
        ATM theATM = new ATM(42, "Gordon College", "First National Bank of Podunk", (InetAddress)null);
        Simulation theSimulation = new Simulation(theATM);
        (new Thread(theATM)).start();
        Panel gui = theSimulation.getGUI();
        this.setBackground(gui.getBackground());
        this.add(gui);
    }
}

