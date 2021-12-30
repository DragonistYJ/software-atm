package scu.software.banking;

public class Message {
    public static final int WITHDRAWAL = 0;
    public static final int INITIATE_DEPOSIT = 1;
    public static final int COMPLETE_DEPOSIT = 2;
    public static final int TRANSFER = 3;
    public static final int INQUIRY = 4;
    private int messageCode;
    private Card card;
    private int pin;
    private int serialNumber;
    private int fromAccount;
    private int toAccount;
    private Money amount;

    public Message(int messageCode, Card card, int pin, int serialNumber, int fromAccount, int toAccount, Money amount) {
        this.messageCode = messageCode;
        this.card = card;
        this.pin = pin;
        this.serialNumber = serialNumber;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public String toString() {
        String result = "";
        switch(this.messageCode) {
            case 0:
                result = result + "WITHDRAW";
                break;
            case 1:
                result = result + "INIT_DEP";
                break;
            case 2:
                result = result + "COMP_DEP";
                break;
            case 3:
                result = result + "TRANSFER";
                break;
            case 4:
                result = result + "INQUIRY ";
        }

        result = result + " CARD# " + this.card.getNumber();
        result = result + " TRANS# " + this.serialNumber;
        if (this.fromAccount >= 0) {
            result = result + " FROM  " + this.fromAccount;
        } else {
            result = result + " NO FROM";
        }

        if (this.toAccount >= 0) {
            result = result + " TO  " + this.toAccount;
        } else {
            result = result + " NO TO";
        }

        if (!this.amount.lessEqual(new Money(0))) {
            result = result + " " + this.amount;
        } else {
            result = result + " NO AMOUNT";
        }

        return result;
    }

    public void setPIN(int pin) {
        this.pin = pin;
    }

    public int getMessageCode() {
        return this.messageCode;
    }

    public Card getCard() {
        return this.card;
    }

    public int getPIN() {
        return this.pin;
    }

    public int getSerialNumber() {
        return this.serialNumber;
    }

    public int getFromAccount() {
        return this.fromAccount;
    }

    public int getToAccount() {
        return this.toAccount;
    }

    public Money getAmount() {
        return this.amount;
    }
}

