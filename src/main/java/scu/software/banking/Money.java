package scu.software.banking;

public class Money {
    private long cents;

    public Money(int dollars) {
        this(dollars, 0);
    }

    public Money(int dollars, int cents) {
        this.cents = 100L * (long)dollars + (long)cents;
    }

    public Money(Money toCopy) {
        this.cents = toCopy.cents;
    }

    public String toString() {
        return "$" + this.cents / 100L + (this.cents % 100L >= 10L ? "." + this.cents % 100L : ".0" + this.cents % 100L);
    }

    public void add(Money amountToAdd) {
        this.cents += amountToAdd.cents;
    }

    public void subtract(Money amountToSubtract) {
        this.cents -= amountToSubtract.cents;
    }

    public boolean lessEqual(Money compareTo) {
        return this.cents <= compareTo.cents;
    }
}

