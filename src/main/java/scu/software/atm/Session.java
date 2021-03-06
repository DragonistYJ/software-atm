package scu.software.atm;

import scu.software.atm.physical.CustomerConsole;
import scu.software.atm.transaction.Transaction;
import scu.software.banking.Card;
import scu.software.banking.Status;
import scu.software.simulation.Simulation;

public class Session {
    private final ATM atm;
    private int pin;
    private int state;
    private static final int READING_CARD_STATE = 1;
    private static final int READING_PIN_STATE = 2;
    private static final int CHOOSING_TRANSACTION_STATE = 3;
    private static final int PERFORMING_TRANSACTION_STATE = 4;
    private static final int EJECTING_CARD_STATE = 5;
    private static final int FINAL_STATE = 6;

    public Session(ATM atm) {
        this.atm = atm;
        this.state = 1;
    }

    public void performSession() {
        Card card = null;
        Transaction currentTransaction = null;


        while (this.state != 6) {
            switch (this.state) {
                case 1:  // 正在读取卡号
                    card = this.atm.getCardReader().readCard();
                    if (card != null) {
                        this.state = 2;
                    } else {
                        this.atm.getCustomerConsole().display("Unable to read card");
                        this.state = 5;
                    }
                    break;
                case 2:  // 正在读取密码
                    try {
                        this.pin = this.atm.getCustomerConsole().readPIN("Please enter your PIN\nThen press ENTER");
                        Status stat = Simulation.getInstance().getSimulatedBank().checkPIN(card, this.pin);
                        for (int i = 0; i < 2 && stat.isInvalidPIN(); ++i) {
                            this.pin = this.atm.getCustomerConsole().readPIN("PIN was incorrect\nPlease re-enter your PIN\nThen press ENTER");
                            this.atm.getCustomerConsole().display("");
                            stat = Simulation.getInstance().getSimulatedBank().checkPIN(card, this.pin);
                        }

                        if (stat.isInvalidPIN()) {
                            this.atm.getCardReader().retainCard();
                            this.atm.getCustomerConsole().display("Your card has been retained\nPlease contact the bank.");

                            try {
                                Thread.sleep(5000L);
                            } catch (InterruptedException ignored) {
                            }

                            this.atm.getCustomerConsole().display("");
                            this.state = 6;
                        } else {
                            if (!stat.isSuccess()) {
                                this.atm.getCustomerConsole().display(stat.toString());
                                this.state = 5;
                            } else {
                                this.state = 3;
                            }
                        }
                    } catch (CustomerConsole.Cancelled var8) {
                        this.state = 5;
                    }
                    break;
                case 3:  // 选择四种操作之一:withdraw（取）,deposit（存）,transfer（转账）,balance inquiry（余额查询）
                    try {
                        currentTransaction = Transaction.makeTransaction(this.atm, this, card, this.pin);
                        this.state = 4;
                    } catch (CustomerConsole.Cancelled var6) {
                        this.state = 5;
                    }
                    break;
                case 4:  // 执行事务逻辑
                    try {
                        boolean doAgain = currentTransaction.performTransaction();
                        if (doAgain) {
                            this.state = 3;
                        } else {
                            this.state = 5;
                        }
                    } catch (Transaction.CardRetained var5) {
                        this.state = 6;
                    }
                    break;
                case 5:  // 弹出银行卡
                    this.atm.getCardReader().ejectCard();
                    this.state = 6;
            }
        }
    }

    public void setPIN(int pin) {
        this.pin = pin;
    }
}

