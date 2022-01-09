package scu.software.simulation;

import scu.software.banking.*;

public class SimulatedBank {
    private static final int[] PIN = new int[]{0, 42, 1234};
    /**
     * card=1 {1,2,0}=>{check, saving, no}
     * card=2 {1,0,3}=>{check, no, market}
     */
    private static final int[][] ACCOUNT_NUMBER = new int[][]{new int[3], {1, 2, 0}, {1, 0, 3}};
    private static final Money[] WITHDRAWALS_TODAY = new Money[]{new Money(0), new Money(0), new Money(0)};
    private static final Money DAILY_WITHDRAWAL_LIMIT = new Money(300);

    // 初始化的3种类型账户里的余额
    private final Money[] BALANCE = new Money[]{new Money(0), new Money(100), new Money(1000), new Money(5000)};
    private final Money[] AVAILABLE_BALANCE = new Money[]{new Money(0), new Money(100), new Money(1000), new Money(5000)};

    public SimulatedBank() {
    }

    public Status handleMessage(Message message, Balances balances) {
        int cardNumber = message.getCard().getNumber();
        if (cardNumber >= 1 && cardNumber <= PIN.length) {
            if (message.getPIN() != PIN[cardNumber]) {
                return new SimulatedBank.InvalidPIN();
            } else {
                switch(message.getMessageCode()) {
                    case 0:
                        return this.withdrawal(message, balances);
                    case 1:
                        return this.initiateDeposit(message);
                    case 2:
                        return this.completeDeposit(message, balances);
                    case 3:
                        return this.transfer(message, balances);
                    case 4:
                        return this.inquiry(message, balances);
                    default:
                        return null;
                }
            }
        } else {
            return new SimulatedBank.Failure("Invalid card");
        }
    }

    private Status withdrawal(Message message, Balances balances) {
        int cardNumber = message.getCard().getNumber();
        int accountNumber = ACCOUNT_NUMBER[cardNumber][message.getFromAccount()];
        if (accountNumber == 0) {
            return new SimulatedBank.Failure("Invalid account type");
        } else {
            Money amount = message.getAmount();
            Money limitRemaining = new Money(DAILY_WITHDRAWAL_LIMIT);
            limitRemaining.subtract(WITHDRAWALS_TODAY[cardNumber]);
            if (!amount.lessEqual(limitRemaining)) {
                return new SimulatedBank.Failure("Daily withdrawal limit exceeded");
            } else if (!amount.lessEqual(this.AVAILABLE_BALANCE[accountNumber])) {
                return new SimulatedBank.Failure("Insufficient available balance");
            } else {
                WITHDRAWALS_TODAY[cardNumber].add(amount);
                this.BALANCE[accountNumber].subtract(amount);
                this.AVAILABLE_BALANCE[accountNumber].subtract(amount);
                balances.setBalances(this.BALANCE[accountNumber], this.AVAILABLE_BALANCE[accountNumber]);
                return new SimulatedBank.Success();
            }
        }
    }

    private Status initiateDeposit(Message message) {
        int cardNumber = message.getCard().getNumber();
        int accountNumber = ACCOUNT_NUMBER[cardNumber][message.getToAccount()];
        return accountNumber == 0 ? new Failure("Invalid account type") : new Success();
    }

    private Status completeDeposit(Message message, Balances balances) {
        int cardNumber = message.getCard().getNumber();
        int accountNumber = ACCOUNT_NUMBER[cardNumber][message.getToAccount()];
        if (accountNumber == 0) {
            return new SimulatedBank.Failure("Invalid account type");
        } else {
            Money amount = message.getAmount();
            this.BALANCE[accountNumber].add(amount);
            // 无缘无故减10元，手续费？
            // this.BALANCE[accountNumber].subtract(new Money(10, 0));
            balances.setBalances(this.BALANCE[accountNumber], this.AVAILABLE_BALANCE[accountNumber]);
            return new SimulatedBank.Success();
        }
    }

    private Status transfer(Message message, Balances balances) {
        int cardNumber = message.getCard().getNumber();
        int fromAccountNumber = ACCOUNT_NUMBER[cardNumber][message.getFromAccount()];
        if (fromAccountNumber == 0) {
            return new SimulatedBank.Failure("Invalid from account type");
        } else {
            int toAccountNumber = ACCOUNT_NUMBER[cardNumber][message.getToAccount()];
            if (toAccountNumber == 0) {
                return new SimulatedBank.Failure("Invalid to account type");
            } else if (fromAccountNumber == toAccountNumber) {
                return new SimulatedBank.Failure("Can't transfer money from\nan account to itself");
            } else {
                Money amount = message.getAmount();
                if (!amount.lessEqual(this.AVAILABLE_BALANCE[fromAccountNumber])) {
                    return new SimulatedBank.Failure("Insufficient available balance");
                } else {
                    this.BALANCE[fromAccountNumber].subtract(amount);
                    this.AVAILABLE_BALANCE[fromAccountNumber].subtract(amount);
                    this.BALANCE[toAccountNumber].add(amount);
                    this.AVAILABLE_BALANCE[toAccountNumber].add(amount);
                    balances.setBalances(this.BALANCE[toAccountNumber], this.AVAILABLE_BALANCE[toAccountNumber]);
                    return new SimulatedBank.Success();
                }
            }
        }
    }

    private Status inquiry(Message message, Balances balances) {
        int cardNumber = message.getCard().getNumber();
        int accountNumber = ACCOUNT_NUMBER[cardNumber][message.getFromAccount()];
        if (accountNumber == 0) {
            return new SimulatedBank.Failure("Invalid account type");
        } else {
            balances.setBalances(this.BALANCE[accountNumber], this.AVAILABLE_BALANCE[accountNumber]);
            return new SimulatedBank.Success();
        }
    }

    public Status checkPIN(Card card, int pin) {
        if (card.getNumber() >= 1 && card.getNumber() <= PIN.length) {
            return pin != PIN[card.getNumber()] ? new InvalidPIN() : new Success();
        } else {
            return new SimulatedBank.Failure("Invalid card");
        }
    }

    private static class Failure extends Status {
        private final String message;

        public Failure(String message) {
            this.message = message;
        }

        public boolean isSuccess() {
            return false;
        }

        public boolean isInvalidPIN() {
            return false;
        }

        public String getMessage() {
            return this.message;
        }
    }

    private static class InvalidPIN extends SimulatedBank.Failure {
        public InvalidPIN() {
            super("Invalid PIN");
        }

        public boolean isInvalidPIN() {
            return true;
        }
    }

    private static class Success extends Status {
        private Success() {
        }

        public boolean isSuccess() {
            return true;
        }

        public boolean isInvalidPIN() {
            return false;
        }

        public String getMessage() {
            return null;
        }
    }
}

