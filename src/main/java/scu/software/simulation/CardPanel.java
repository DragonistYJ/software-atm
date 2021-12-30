package scu.software.simulation;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class CardPanel extends Panel {
    private TextField cardNumberField;

    CardPanel() {
        this.setLayout(new GridLayout(0, 1, 0, 0));
        this.setFont(new Font("Monospaced", 0, 14));
        this.add(new Label("A real ATM would have a magnetic", 1));
        this.add(new Label("stripe reader to read the card", 1));
        this.add(new Label("For purposes of the simulation,", 1));
        this.add(new Label("please enter the card number manually.", 1));
        this.add(new Label("Then press RETURN", 1));
        this.add(new Label("(An invalid integer or an integer not", 1));
        this.add(new Label("greater than zero will be treated as", 1));
        this.add(new Label("an unreadable card)", 1));
        this.cardNumberField = new TextField(30);
        this.cardNumberField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                synchronized(CardPanel.this) {
                    CardPanel.this.notify();
                }
            }
        });
        Panel cardNumberPanel = new Panel();
        cardNumberPanel.add(this.cardNumberField);
        this.add(cardNumberPanel);
    }

    synchronized int readCardNumber() {
        this.cardNumberField.setText("");
        this.cardNumberField.requestFocus();

        try {
            this.wait();
        } catch (InterruptedException var4) {
        }

        int cardNumber;
        try {
            cardNumber = Integer.parseInt(this.cardNumberField.getText());
            if (cardNumber <= 0) {
                cardNumber = -1;
            }
        } catch (NumberFormatException var3) {
            cardNumber = -1;
        }

        return cardNumber;
    }
}

