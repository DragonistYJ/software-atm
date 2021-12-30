package scu.software.simulation;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SimOperatorPanel extends Panel {
    SimOperatorPanel(final Simulation simulation) {
        this.setLayout(new BorderLayout(10, 0));
        this.setBackground(new Color(128, 128, 255));
        this.add(new Label("     Operator Panel"), "West");
        final Label message = new Label("Click button to turn ATM on", 1);
        this.add(message, "Center");
        final Button button = new Button(" ON ");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (button.getLabel().equals("OFF")) {
                    message.setText("Click button to turn ATM on  ");
                    button.setLabel(" ON ");
                    simulation.switchChanged(false);
                } else {
                    message.setText("Click button to turn ATM off");
                    button.setLabel("OFF");
                    simulation.switchChanged(true);
                }

            }
        });
        Panel buttonPanel = new Panel();
        buttonPanel.add(button);
        this.add(buttonPanel, "East");
        (new Thread() {
            public void run() {
                while(true) {
                    try {
                        sleep(1000L);
                    } catch (InterruptedException var2) {
                    }

                    if ((!message.isVisible() || button.getLabel().equals("OFF")) && SimOperatorPanel.this.isEnabled()) {
                        message.setVisible(true);
                    } else {
                        message.setVisible(false);
                    }
                }
            }
        }).start();
    }
}

