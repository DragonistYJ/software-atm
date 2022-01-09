package scu.software.banking;

public class Balances {
    private Money total;
    private Money available;

    public Balances() {
    }

    public void setBalances(Money total, Money available) {
        this.total = total;
        this.available = available;
    }

    public Money getTotal() {
        return this.total;
    }

    public Money getAvailable() {
        return this.available;
    }

    @Override
    public String toString() {
        return "Balances{" +
                "total=" + total +
                ", available=" + available +
                '}';
    }
}
