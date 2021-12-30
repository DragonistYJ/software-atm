package scu.software.simulation;

import scu.software.atm.ATM;
import scu.software.banking.*;

public class Simulation {
    public static final int PIN_MODE = 1;
    public static final int AMOUNT_MODE = 2;
    public static final int MENU_MODE = 3;
    private ATM atm;
    private SimOperatorPanel operatorPanel;
    private SimCardReader cardReader;
    private SimDisplay display;
    private SimKeyboard keyboard;
    private SimCashDispenser cashDispenser;
    private SimEnvelopeAcceptor envelopeAcceptor;
    private SimReceiptPrinter receiptPrinter;
    private GUI gui;
    private SimulatedBank simulatedBank;
    private static Simulation theInstance;

    public Simulation(ATM atm) {
        this.atm = atm;
        this.operatorPanel = new SimOperatorPanel(this);
        this.cardReader = new SimCardReader(this);
        this.display = new SimDisplay();
        this.cashDispenser = new SimCashDispenser();
        this.envelopeAcceptor = new SimEnvelopeAcceptor();
        this.receiptPrinter = new SimReceiptPrinter();
        this.keyboard = new SimKeyboard(this.display, this.envelopeAcceptor, atm);
        this.gui = new GUI(this.operatorPanel, this.cardReader, this.display, this.keyboard, this.cashDispenser, this.envelopeAcceptor, this.receiptPrinter);
        this.simulatedBank = new SimulatedBank();
        theInstance = this;
    }

    public static Simulation getInstance() {
        return theInstance;
    }

    public Money getInitialCash() {
        return this.gui.getInitialCash();
    }

    public Card readCard() {
        this.operatorPanel.setEnabled(false);
        this.cardReader.animateInsertion();
        return this.gui.readCard();
    }

    public void ejectCard() {
        this.cardReader.animateEjection();
        this.operatorPanel.setEnabled(true);
    }

    public void retainCard() {
        this.cardReader.animateRetention();
        this.operatorPanel.setEnabled(true);
    }

    public void clearDisplay() {
        this.display.clearDisplay();
    }

    public void display(String text) {
        this.display.display(text);
    }

    public String readInput(int mode, int maxValue) {
        return this.keyboard.readInput(mode, maxValue);
    }

    public void dispenseCash(Money amount) {
        this.cashDispenser.animateDispensingCash(amount);
    }

    public boolean acceptEnvelope() {
        return this.envelopeAcceptor.acceptEnvelope();
    }

    public void printReceiptLine(String text) {
        this.receiptPrinter.println(text);
    }

    public void printLogLine(String text) {
        this.gui.printLogLine(text);
    }

    public Status sendMessage(Message message, Balances balances) {
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException var4) {
        }

        return this.simulatedBank.handleMessage(message, balances);
    }

    void switchChanged(boolean on) {
        this.cardReader.setVisible(on);
        if (on) {
            this.atm.switchOn();
        } else {
            this.atm.switchOff();
        }

    }

    void cardInserted() {
        this.atm.cardInserted();
    }

    public GUI getGUI() {
        return this.gui;
    }

    public SimulatedBank getSimulatedBank() {
        return this.simulatedBank;
    }
}

