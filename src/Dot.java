import java.awt.*;

public class Dot extends Component {

    private int x;
    private int y;

    public Dot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g, Color c) {
        g.setColor(c);
        g.drawLine(x / 2, -(y / 1000000) + 400, x / 2, -(y / 1000000) + 400);
    }
}
