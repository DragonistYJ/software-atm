package scu.software.simulation;

import scu.software.atm.ATM;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class SimKeyboard extends Panel {
    private final SimDisplay display;
    private final SimEnvelopeAcceptor envelopeAcceptor;
    private int mode;
    private static final int IDLE_MODE = 0;
    private static final int PIN_MODE = 1;
    private static final int AMOUNT_MODE = 2;
    private static final int MENU_MODE = 3;
    private final StringBuffer currentInput;
    private boolean cancelled;
    private int maxValue;
    private final ATM atm;

    SimKeyboard(SimDisplay display, SimEnvelopeAcceptor envelopeAcceptor, ATM atm) {
        this.atm = atm;
        this.display = display;
        this.envelopeAcceptor = envelopeAcceptor;
        this.setLayout(new GridLayout(5, 3));
        Button[] digitKey = new Button[10];

        for (int i = 1; i < 10; ++i) {
            digitKey[i] = new Button("" + i);
            this.add(digitKey[i]);
        }

        this.add(new Label(""));
        digitKey[0] = new Button("0");
        this.add(digitKey[0]);
        this.add(new Label(""));
        Button enterKey = new Button("ENTER");
        enterKey.setForeground(Color.black);
        enterKey.setBackground(new Color(128, 128, 255));
        this.add(enterKey);
        Button clearKey = new Button("CLEAR");
        clearKey.setForeground(Color.black);
        clearKey.setBackground(new Color(255, 128, 128));
        this.add(clearKey);
        Button cancelKey = new Button("CANCEL");
        cancelKey.setBackground(Color.red);
        cancelKey.setForeground(Color.black);
        this.add(cancelKey);

        for (int i = 0; i < 10; ++i) {
            digitKey[i].addActionListener(e -> SimKeyboard.this.digitKeyPressed(Integer.parseInt(e.getActionCommand())));
        }

        enterKey.addActionListener(e -> SimKeyboard.this.enterKeyPressed());
        clearKey.addActionListener(e -> SimKeyboard.this.clearKeyPressed());
        cancelKey.addActionListener(e -> SimKeyboard.this.cancelKeyPressed());
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                char keyChar = e.getKeyChar();
                int keyCode = e.getKeyCode();
                if (keyChar >= '0' && keyChar <= '9') {
                    SimKeyboard.this.digitKeyPressed(keyChar - 48);
                    e.consume();
                } else {
                    switch (keyCode) {
                        case 3:
                        case 27:
                            SimKeyboard.this.cancelKeyPressed();
                            break;
                        case 10:
                            SimKeyboard.this.enterKeyPressed();
                            break;
                        case 12:
                            SimKeyboard.this.clearKeyPressed();
                    }
                    e.consume();
                }

            }
        });
        this.currentInput = new StringBuffer();
        this.mode = 0;
    }

    synchronized String readInput(int mode, int maxValue) {
        this.mode = mode;
        this.maxValue = maxValue;
        this.currentInput.setLength(0);
        this.cancelled = false;
        if (mode == 2) {
            this.setEcho("0.00");
        } else {
            this.setEcho("");
        }

        this.requestFocus();

        try {
            this.wait();
        } catch (InterruptedException ignored) {
        }

        this.mode = 0;
        return this.cancelled ? null : this.currentInput.toString();
    }

    private synchronized void digitKeyPressed(int digit) {
        switch (this.mode) {
            case 0:
            default:
                break;
            case 1: // 输入密码
                this.currentInput.append(digit);
                StringBuilder echoString = new StringBuilder();

                for (int i = 0; i < this.currentInput.length(); ++i) {
                    echoString.append('*');
                }

                this.setEcho(echoString.toString());
                break;
            case 2: // 输入金额
                this.currentInput.append(digit);
                String input = this.currentInput.toString();
                if (input.length() == 1) {
                    this.setEcho("0.0" + input);
                } else if (input.length() == 2) {
                    this.setEcho("0." + input);
                } else {
                    this.setEcho(input.substring(0, input.length() - 2) + "." + input.substring(input.length() - 2));
                }
                break;
            case 3: // 选择模式
                this.currentInput.append(digit);
                if (digit < 0 || digit > this.maxValue) {
                    this.getToolkit().beep();
                }
                this.notify();
        }

    }

    private synchronized void enterKeyPressed() {
        switch (this.mode) {
            case 0:
            default:
                break;
            case 1:
            case 2:
                if (this.currentInput.length() > 0) {
                    this.notify();
                } else {
                    this.getToolkit().beep();
                }
                break;
            case 3:
                this.getToolkit().beep();
        }

    }

    private synchronized void clearKeyPressed() {
        switch (this.mode) {
            case 0:
            default:
                break;
            case 1:
                this.currentInput.setLength(0);
                this.setEcho("");
                break;
            case 2:
                this.currentInput.setLength(0);
                this.setEcho("0.00");
                break;
            case 3:
                this.getToolkit().beep();
        }

    }

    private synchronized void cancelKeyPressed() {
        switch (this.mode) {
            case 0:
                synchronized (this.envelopeAcceptor) {
                    this.envelopeAcceptor.notify();
                }
            case 1:
            case 2:
            case 3:
                this.cancelled = true;
                this.notify();
            default:
        }
    }

    private void setEcho(String echo) {
        this.display.setEcho(echo);
    }
}

