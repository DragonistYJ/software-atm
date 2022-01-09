package scu.software;

import scu.software.atm.ATM;
import scu.software.simulation.Simulation;

import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;

public class ATMMain {
    public ATMMain() {
    }

    public static void main(String[] args) {
        ATM theATM = new ATM(42, "Gordon College", "First National Bank of Podunk", null);
        Simulation theSimulation = new Simulation(theATM);
        Frame mainFrame = new Frame("ATM Simulation");
        mainFrame.add(theSimulation.getGUI());
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem quitItem = new MenuItem("Quit", new MenuShortcut(81));
        quitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(quitItem);
        menuBar.add(fileMenu);
        mainFrame.setMenuBar(menuBar);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        (new Thread(theATM)).start();
        mainFrame.setResizable(false);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
