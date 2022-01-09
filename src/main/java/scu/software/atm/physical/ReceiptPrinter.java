package scu.software.atm.physical;

import scu.software.banking.Receipt;
import scu.software.simulation.Simulation;

import java.util.Enumeration;

public class ReceiptPrinter {
    public ReceiptPrinter() {
    }

    public void printReceipt(Receipt receipt) {
        Enumeration<String> receiptLines = receipt.getLines();

        while (receiptLines.hasMoreElements()) {
            Simulation.getInstance().printReceiptLine(receiptLines.nextElement());
        }
    }
}
