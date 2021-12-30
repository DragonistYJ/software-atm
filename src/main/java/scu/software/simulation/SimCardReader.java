package scu.software.simulation;

import java.awt.Button;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SimCardReader extends Button {
    private Rectangle originalBounds;

    SimCardReader(final Simulation simulation) {
        super("Click to insert card");
        this.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulation.cardInserted();
            }
        });
        this.setVisible(false);
    }

    void animateInsertion() {
        this.originalBounds = this.getBounds();

        for(Rectangle currentBounds = new Rectangle(this.originalBounds.x, this.originalBounds.y, this.originalBounds.width, this.originalBounds.height); currentBounds.width > 0 && currentBounds.height > 0; currentBounds.y = this.originalBounds.y + (this.originalBounds.height - currentBounds.height) / 2) {
            this.setBounds(currentBounds.x, currentBounds.y, currentBounds.width, currentBounds.height);
            this.repaint();

            try {
                Thread.sleep(100L);
            } catch (InterruptedException var3) {
            }

            --currentBounds.height;
            currentBounds.width = this.originalBounds.width * currentBounds.height / this.originalBounds.height;
            currentBounds.x = this.originalBounds.x + (this.originalBounds.width - currentBounds.width) / 2;
        }

        this.setVisible(false);
    }

    void animateEjection() {
        this.setLabel("Ejecting card");
        this.setVisible(true);

        for(Rectangle currentBounds = new Rectangle(this.originalBounds.x + this.originalBounds.width / 2, this.originalBounds.y + this.originalBounds.height / 2, this.originalBounds.width / this.originalBounds.height, 1); currentBounds.height <= this.originalBounds.height && currentBounds.width <= this.originalBounds.width; currentBounds.y = this.originalBounds.y + (this.originalBounds.height - currentBounds.height) / 2) {
            this.setBounds(currentBounds.x, currentBounds.y, currentBounds.width, currentBounds.height);
            this.repaint();

            try {
                Thread.sleep(100L);
            } catch (InterruptedException var3) {
            }

            ++currentBounds.height;
            currentBounds.width = this.originalBounds.width * currentBounds.height / this.originalBounds.height;
            currentBounds.x = this.originalBounds.x + (this.originalBounds.width - currentBounds.width) / 2;
        }

        this.setLabel("Click to insert card");
    }

    void animateRetention() {
        this.setLabel("Click to insert card");
        this.setVisible(true);
    }
}

