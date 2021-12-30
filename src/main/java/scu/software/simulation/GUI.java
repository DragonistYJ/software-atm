package scu.software.simulation;

import scu.software.banking.Card;
import scu.software.banking.Money;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Panel;

class GUI extends Panel {
    private CardLayout mainLayout;
    private ATMPanel atmPanel;
    private BillsPanel billsPanel;
    private CardPanel cardPanel;
    private LogPanel logPanel;

    GUI(SimOperatorPanel operatorPanel, SimCardReader cardReader, SimDisplay display, SimKeyboard keyboard, SimCashDispenser cashDispenser, SimEnvelopeAcceptor envelopeAcceptor, SimReceiptPrinter receiptPrinter) {
        this.setBackground(Color.lightGray);
        this.mainLayout = new CardLayout(5, 5);
        this.setLayout(this.mainLayout);
        this.atmPanel = new ATMPanel(this, operatorPanel, cardReader, display, keyboard, cashDispenser, envelopeAcceptor, receiptPrinter);
        this.add(this.atmPanel, "ATM");
        this.billsPanel = new BillsPanel();
        this.add(this.billsPanel, "BILLS");
        this.cardPanel = new CardPanel();
        this.add(this.cardPanel, "CARD");
        this.logPanel = new LogPanel(this);
        this.add(this.logPanel, "LOG");
        this.mainLayout.show(this, "ATM");
    }

    public Money getInitialCash() {
        this.mainLayout.show(this, "BILLS");
        int numberOfBills = this.billsPanel.readBills();
        this.mainLayout.show(this, "ATM");
        return new Money(20 * numberOfBills);
    }

    public Card readCard() {
        this.mainLayout.show(this, "CARD");
        int cardNumber = this.cardPanel.readCardNumber();
        this.mainLayout.show(this, "ATM");
        return cardNumber > 0 ? new Card(cardNumber) : null;
    }

    public void printLogLine(String text) {
        this.logPanel.println(text);
    }

    void showCard(String cardName) {
        this.mainLayout.show(this, cardName);
    }

    static GridBagConstraints makeConstraints(int row, int col, int width, int height, int fill) {
        GridBagConstraints g = new GridBagConstraints();
        g.gridy = row;
        g.gridx = col;
        g.gridheight = height;
        g.gridwidth = width;
        g.fill = fill;
        g.insets = new Insets(2, 2, 2, 2);
        g.weightx = 1.0D;
        g.weighty = 1.0D;
        g.anchor = 10;
        return g;
    }
}

