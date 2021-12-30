package scu.software.simulation;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class BillsPanel extends Panel {
    private TextField billsNumberField;

    BillsPanel() {
        this.setLayout(new GridLayout(0, 1, 0, 0));
        this.setFont(new Font("Monospaced", 0, 14));
        this.add(new Label("A real ATM would have a mechanism to sense", 1));
        this.add(new Label("or allow the operator to enter the number", 1));
        this.add(new Label("of $20 bills in the cash dispenser.", 1));
        this.add(new Label("For purposes of the simulation,", 1));
        this.add(new Label("please enter the number of $20 bills manually.", 1));
        this.add(new Label("Then press RETURN", 1));
        this.billsNumberField = new TextField(30);
        this.billsNumberField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                synchronized(BillsPanel.this) {
                    BillsPanel.this.notify();
                }
            }
        });
        Panel billsNumberPanel = new Panel();
        billsNumberPanel.add(this.billsNumberField);
        this.add(billsNumberPanel);
    }

    synchronized int readBills() {
        boolean validNumberRead = false;
        int billsNumber = 0;
        this.billsNumberField.setText("");

        while(!validNumberRead) {
            this.billsNumberField.requestFocus();

            try {
                this.wait();
            } catch (InterruptedException var5) {
            }

            try {
                billsNumber = Integer.parseInt(this.billsNumberField.getText());
                if (billsNumber >= 0) {
                    validNumberRead = true;
                } else {
                    this.getToolkit().beep();
                }
            } catch (NumberFormatException var4) {
                this.getToolkit().beep();
            }

            if (!validNumberRead) {
                this.billsNumberField.setText("Must be a valid integer >= 0");
                this.billsNumberField.selectAll();
            }
        }

        return billsNumber;
    }
}

