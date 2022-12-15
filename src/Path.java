import java.util.Vector;

/**
 * This class holds a found path from the start to the end node.
 * The actual path is constructed by calling the reconstructPath(Node end) method
 * in the SearchMethod base class.
 *
 * @author Johan Hagelbäck
 */
public class Path {
    /**
     * The list of nodes in the path
     */
    public Vector<Node> path;
    /**
     * The total length of the path
     */
    private double length = 0;

    /**
     * Constructor.
     */
    public Path() {
        path = new Vector<Node>();
        length = 0;
    }

    /**
     * Adds a new node to the path. Called when reconstructing a path after
     * a pathfinding search.
     *
     * @param n The end node
     */
    public void add(Node n) {
        path.add(0, n);

        if (path.size() >= 2) {
            length += getLength(path.elementAt(0), path.elementAt(1));
        }
    }

    /**
     * Adds a new node last in the path. Called when reconstructing a path after
     * a pathfinding search.
     *
     * @param n The end node
     */
    public void addLast(Node n) {
        path.add(n);

        if (path.size() >= 2) {
            length += getLength(path.elementAt(0), path.elementAt(1));
        }
    }

    /**
     * Resets and recalculates the path length.
     */
    public void recalculateLength() {
        length = 0;

        for (int i = 0; i < path.size() - 1; i++) {
            length += getLength(path.elementAt(i), path.elementAt(i + 1));
        }
    }

    /**
     * Calculates the length (Euclidean distance) between two nodes using
     * Pythagoras theorem.
     *
     * @param a The first node
     * @param b The second node
     * @return The distance between the nodes
     */
    private double getLength(Node a, Node b) {
        double sqX = Math.pow(b.x - a.x, 2);
        double sqY = Math.pow(b.y - a.y, 2);
        double euclidean = Math.sqrt(sqX + sqY);
        return euclidean;
    }

    /**
     * Returns the total length of the path.
     *
     * @return Total path length
     */
    public double getLength() {
        return length;
    }

    /**
     * Returns the number of segments (links) in the path.
     *
     * @return Number of segments (links)
     */
    public int getPieces() {
        return path.size() - 1;
    }

    /**
     * Show the links in the path. This method is used by the GUI to visualize
     * the found path.
     */
    public void showPathLinks() {
        for (int i = 0; i < path.size() - 1; i++) {
            Node a = path.get(i);
            Node b = path.get(i + 1);

            for (Link l : Map.getInstance().getLinks()) {
                if (l.a.equals(a) && l.b.equals(b)) {
                    l.visibility = Link.INPATH;
                }
            }
        }
    }
}
    
