import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * GUI panel for drawing the map.
 *
 * @author Johan Hagelbäck
 */
public class MapPanel extends JPanel {
    private Map map;

    public MapPanel() {
        this.setPreferredSize(new Dimension(550, 410));
        map = Map.getInstance();
    }

    public void paint(Graphics gn) {
        Graphics2D g = (Graphics2D) gn;

        g.setColor(Color.white);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        Vector<Link> links = map.getLinks();
        //Draw black links
        g.setStroke(new BasicStroke(1));
        g.setColor(Color.black);
        for (Link l : links) {
            if (l.visibility == Link.NONE) {
                g.drawLine(l.getDrawX1(), l.getDrawY1(), l.getDrawX2(), l.getDrawY2());
            }
        }
        //Draw visited and in path links
        for (Link l : links) {
            if (l.visibility != Link.NONE) {
                if (l.visibility == Link.VISITED) {
                    g.setStroke(new BasicStroke(3));
                    g.setColor(new Color(39, 117, 219));
                }
                if (l.visibility == Link.INPATH) {
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.red);
                }
                g.drawLine(l.getDrawX1(), l.getDrawY1(), l.getDrawX2(), l.getDrawY2());
            }
        }

        Vector<Node> nodes = map.getNodes();
        for (Node n : nodes) {
            g.setStroke(new BasicStroke(1));
            g.setColor(Color.lightGray);
            if (n.isStart) g.setColor(Color.green);
            if (n.isEnd) g.setColor(new Color(255, 130, 150));
            g.fillOval(n.getDrawX(), n.getDrawY(), 16, 16);

            g.setColor(Color.black);
            g.drawOval(n.getDrawX(), n.getDrawY(), 16, 16);

            g.setFont(new Font("Arial", Font.PLAIN, 10));
            g.drawString(n.label, n.getDrawLabelX(), n.getDrawLabelY());
        }
    }
}