package scu.software.atm.physical;

import scu.software.banking.Money;
import scu.software.simulation.Simulation;

public class CustomerConsole {
    public CustomerConsole() {
    }

    public void display(String message) {
        Simulation.getInstance().clearDisplay();
        Simulation.getInstance().display(message);
    }

    public int readPIN(String prompt) throws Cancelled {
        Simulation.getInstance().clearDisplay();
        Simulation.getInstance().display(prompt);
        Simulation.getInstance().display("");
        String input = Simulation.getInstance().readInput(1, 0);
        Simulation.getInstance().clearDisplay();
        if (input == null) {
            throw new Cancelled();
        } else {
            return Integer.parseInt(input);
        }
    }

    public synchronized int readMenuChoice(String prompt, String[] menu) throws Cancelled {
        Simulation.getInstance().clearDisplay();
        Simulation.getInstance().display(prompt);

        for(int i = 0; i < menu.length; ++i) {
            Simulation.getInstance().display(i + 1 + ") " + menu[i]);
        }

        String input = Simulation.getInstance().readInput(3, menu.length);
        Simulation.getInstance().clearDisplay();
        if (input == null) {
            throw new Cancelled();
        } else {
            return Integer.parseInt(input) - 1;
        }
    }

    public synchronized Money readAmount(String prompt) throws Cancelled {
        Simulation.getInstance().clearDisplay();
        Simulation.getInstance().display(prompt);
        Simulation.getInstance().display("");
        String input = Simulation.getInstance().readInput(2, 0);
        Simulation.getInstance().clearDisplay();
        if (input == null) {
            throw new Cancelled();
        } else {
            int dollars = Integer.parseInt(input) / 100;
            int cents = Integer.parseInt(input) % 100;
            return new Money(dollars, cents);
        }
    }

    public static class Cancelled extends Exception {
        public Cancelled() {
            super("Cancelled by customer");
        }
    }
}

