
import javax.swing.*;
import java.awt.*;

public class LabelPanel extends JPanel {

    private JLabel deathsLabel;
    private JLabel recoveredLabel;
    private JLabel currentlyInfectedLabel;
    private JLabel allTimeInfectedLabel;

    public LabelPanel() {
        this.setLayout(new GridLayout(1, 4));

        deathsLabel = new JLabel("Deaths: 0");
        recoveredLabel = new JLabel("Recovered: 0");
        currentlyInfectedLabel = new JLabel("Currently Infected: 0");
        allTimeInfectedLabel = new JLabel("Total Infected: 0");

        this.add(deathsLabel);
        this.add(recoveredLabel);
        this.add(currentlyInfectedLabel);
        this.add(allTimeInfectedLabel);

    }

    public void updateLabels(int deaths, int recovered, int currentlyInfected, int allTimeInfected) {
        deathsLabel.setText("Deaths: " + deaths);
        recoveredLabel.setText("Recovered: " + recovered);
        currentlyInfectedLabel.setText("Currently Infected: " + currentlyInfected);
        allTimeInfectedLabel.setText("Total Infected: " + allTimeInfected);
    }

}
