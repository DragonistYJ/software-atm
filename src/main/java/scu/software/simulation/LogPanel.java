package scu.software.simulation;

import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class LogPanel extends Panel {
    private TextArea logPrintArea;

    LogPanel(final GUI gui) {
        GridBagLayout logLayout = new GridBagLayout();
        this.setLayout(logLayout);
        this.setFont(new Font("Monospaced", 0, 14));
        Label logPanelLabel = new Label("Log", 1);
        this.add(logPanelLabel);
        GridBagConstraints constraints = GUI.makeConstraints(0, 0, 1, 1, 0);
        constraints.weighty = 0.0D;
        logLayout.setConstraints(logPanelLabel, constraints);
        this.logPrintArea = new TextArea();
        this.logPrintArea.setBackground(Color.white);
        this.logPrintArea.setForeground(Color.black);
        this.logPrintArea.setFont(new Font("Monospaced", 0, 12));
        this.logPrintArea.setEditable(false);
        this.add(this.logPrintArea);
        constraints = GUI.makeConstraints(1, 0, 1, 1, 1);
        constraints.weighty = 1.0D;
        logLayout.setConstraints(this.logPrintArea, constraints);
        Panel logButtonPanel = new Panel();
        logButtonPanel.setLayout(new FlowLayout());
        Button clearLogButton = new Button("Clear Log");
        clearLogButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LogPanel.this.logPrintArea.setText("");
            }
        });
        logButtonPanel.add(clearLogButton);
        Button dismissLogButton = new Button(" Hide Log ");
        dismissLogButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gui.showCard("ATM");
            }
        });
        logButtonPanel.add(dismissLogButton);
        this.add(logButtonPanel);
        constraints = GUI.makeConstraints(2, 0, 1, 1, 0);
        constraints.weighty = 0.0D;
        logLayout.setConstraints(logButtonPanel, constraints);
    }

    void println(String text) {
        this.logPrintArea.append(text + "\n");
    }
}

