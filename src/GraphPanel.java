import javax.swing.*;
import java.awt.*;

public class GraphPanel extends JPanel {

    private static Color INFECTED_COLOR = Color.red;
    private static Color DEAD_COLOR = Color.black;
    private static Color RECOVERED_COLOR = Color.blue;
    private static Color CURRENTLY_INFECTED = Color.MAGENTA;

    private Graph infectedGraph;
    private Graph deadGraph;

    public Graph getCurrentlyInfectedGraph() {
        return currentlyInfectedGraph;
    }

    private Graph currentlyInfectedGraph;

    public Graph getRecoveredGraph() {
        return recoveredGraph;
    }

    private Graph recoveredGraph;

    public GraphPanel() {
    	this.setBackground(Color.LIGHT_GRAY);
        deadGraph = new Graph(DEAD_COLOR);
        recoveredGraph = new Graph(RECOVERED_COLOR);
        currentlyInfectedGraph = new Graph(CURRENTLY_INFECTED);
        infectedGraph = new Graph(INFECTED_COLOR);
    } 

    @Override
    protected void paintComponent(Graphics g) {
        infectedGraph.udpateDraw(g);
        deadGraph.udpateDraw(g);
        recoveredGraph.udpateDraw(g);
        currentlyInfectedGraph.udpateDraw(g);
    }

    public Graph getInfectedGraph() {
        return infectedGraph;
    }

    public Graph getDeadGraph() {
        return deadGraph;
    }

}
