package scu.software.simulation;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.util.StringTokenizer;

class SimDisplay extends Panel {
    private Label[] displayLine;
    private int currentDisplayLine;

    SimDisplay() {
        this.setLayout(new GridLayout(9, 1));
        this.setBackground(new Color(0, 96, 0));
        this.setForeground(Color.white);
        Font lineFont = new Font("Monospaced", 0, 14);
        this.displayLine = new Label[9];

        for(int i = 0; i < 9; ++i) {
            this.displayLine[i] = new Label("                                             ");
            this.displayLine[i].setFont(lineFont);
            this.add(this.displayLine[i]);
        }

        this.currentDisplayLine = 0;
    }

    void clearDisplay() {
        for(int i = 0; i < this.displayLine.length; ++i) {
            this.displayLine[i].setText("");
        }

        this.currentDisplayLine = 0;
    }

    void display(String text) {
        StringTokenizer tokenizer = new StringTokenizer(text, "\n", false);

        while(tokenizer.hasMoreTokens()) {
            try {
                this.displayLine[this.currentDisplayLine++].setText(tokenizer.nextToken());
            } catch (Exception var4) {
            }
        }

    }

    void setEcho(String echo) {
        this.displayLine[this.currentDisplayLine].setText("                                             ".substring(0, "                                             ".length() / 2 - echo.length()) + echo);
    }

    public Insets getInsets() {
        Insets insets = super.getInsets();
        insets.top += 5;
        insets.bottom += 5;
        insets.left += 10;
        insets.right += 10;
        return insets;
    }
}

