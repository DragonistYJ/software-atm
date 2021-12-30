package scu.software.atm;

import scu.software.atm.physical.*;
import scu.software.banking.Money;

import java.net.InetAddress;

public class ATM implements Runnable {
    private int id;
    private String place;
    private String bankName;
    private InetAddress bankAddress;
    private CardReader cardReader;
    private CashDispenser cashDispenser;
    private CustomerConsole customerConsole;
    private EnvelopeAcceptor envelopeAcceptor;
    private Log log;
    private NetworkToBank networkToBank;
    private OperatorPanel operatorPanel;
    private ReceiptPrinter receiptPrinter;
    private int state;
    private boolean switchOn;
    private boolean cardInserted;
    private static final int OFF_STATE = 0;
    private static final int IDLE_STATE = 1;
    private static final int SERVING_CUSTOMER_STATE = 2;

    public ATM(int id, String place, String bankName, InetAddress bankAddress) {
        this.id = id;
        this.place = place;
        this.bankName = bankName;
        this.bankAddress = bankAddress;
        this.log = new Log();
        this.cardReader = new CardReader(this);
        this.cashDispenser = new CashDispenser(this.log);
        this.customerConsole = new CustomerConsole();
        this.envelopeAcceptor = new EnvelopeAcceptor(this.log);
        this.networkToBank = new NetworkToBank(this.log, bankAddress);
        this.operatorPanel = new OperatorPanel(this);
        this.receiptPrinter = new ReceiptPrinter();
        this.state = 0;
        this.switchOn = false;
        this.cardInserted = false;
    }

    public void run() {
        Session currentSession = null;

        while(true) {
            while(true) {
                switch(this.state) {
                    case 0:
                        this.customerConsole.display("Not currently available");
                        synchronized(this) {
                            try {
                                this.wait();
                            } catch (InterruptedException var6) {
                            }
                        }

                        if (this.switchOn) {
                            this.performStartup();
                            this.state = 1;
                        }
                        break;
                    case 1:
                        this.customerConsole.display("Please insert your card");
                        this.cardInserted = false;
                        synchronized(this) {
                            try {
                                this.wait();
                            } catch (InterruptedException var4) {
                            }
                        }

                        if (this.cardInserted) {
                            currentSession = new Session(this);
                            this.state = 2;
                        } else if (!this.switchOn) {
                            this.performShutdown();
                            this.state = 0;
                        }
                        break;
                    case 2:
                        currentSession.performSession();
                        this.state = 1;
                }
            }
        }
    }

    public synchronized void switchOn() {
        this.switchOn = true;
        this.notify();
    }

    public synchronized void switchOff() {
        this.switchOn = false;
        this.notify();
    }

    public synchronized void cardInserted() {
        this.cardInserted = true;
        this.notify();
    }

    public int getID() {
        return this.id;
    }

    public String getPlace() {
        return this.place;
    }

    public String getBankName() {
        return this.bankName;
    }

    public CardReader getCardReader() {
        return this.cardReader;
    }

    public CashDispenser getCashDispenser() {
        return this.cashDispenser;
    }

    public CustomerConsole getCustomerConsole() {
        return this.customerConsole;
    }

    public EnvelopeAcceptor getEnvelopeAcceptor() {
        return this.envelopeAcceptor;
    }

    public Log getLog() {
        return this.log;
    }

    public NetworkToBank getNetworkToBank() {
        return this.networkToBank;
    }

    public OperatorPanel getOperatorPanel() {
        return this.operatorPanel;
    }

    public ReceiptPrinter getReceiptPrinter() {
        return this.receiptPrinter;
    }

    private void performStartup() {
        Money initialCash = this.operatorPanel.getInitialCash();
        this.cashDispenser.setInitialCash(initialCash);
        this.networkToBank.openConnection();
    }

    private void performShutdown() {
        this.networkToBank.closeConnection();
    }
}

