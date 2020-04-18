import java.awt.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Graph {

    private ArrayList<Dot> points;
    private Color lineColor;

    public Graph(Color lineColor) {
        points = new ArrayList<>();
        this.lineColor = lineColor;
    }

    public ArrayList<Dot> getPoints() {
        return points;
    }

    public void udpateDraw(Graphics g) {
        try {
            for (Dot dot : points) {
                dot.draw(g, lineColor);
            }
        } catch (ConcurrentModificationException hhhh) {
        	//System.out.println("Screen Size Adjusted");
        }

    } 
}
