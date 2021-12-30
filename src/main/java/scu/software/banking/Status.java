package scu.software.banking;

public abstract class Status {
    public Status() {
    }

    public String toString() {
        if (this.isSuccess()) {
            return "SUCCESS";
        } else {
            return this.isInvalidPIN() ? "INVALID PIN" : "FAILURE " + this.getMessage();
        }
    }

    public abstract boolean isSuccess();

    public abstract boolean isInvalidPIN();

    public abstract String getMessage();
}
