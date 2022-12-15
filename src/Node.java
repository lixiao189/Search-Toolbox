/**
 * This class represents a node in both the map to find a path in, and a node
 * used by the search methods.
 *
 * @author Johan Hagelbäck
 */
public class Node {
    /**
     * X-coordinate
     */
    public int x;
    /**
     * Y-coordinate
     */
    public int y;
    /**
     * Actual cost from start node to this node
     */
    public double actual;
    /**
     * Parent to this node
     */
    public Node parent;
    /**
     * True if this is the start node
     */
    public boolean isStart = false;
    /**
     * True if this is the end node
     */
    public boolean isEnd = false;
    /**
     * Label for this node
     */
    public String label;

    /**
     * Creates a new node.
     *
     * @param x     X-coordinate
     * @param y     Y-coordinate
     * @param label Label for this node
     */
    public Node(int x, int y, String label) {
        this.x = x;
        this.y = y;
        this.label = label;
        parent = null;
        actual = 0;
    }

    /**
     * Creates a copy of this node object. Useful to avoid problems
     * with changing values in a reference.
     *
     * @return The copy of this node
     */
    public Node clone() {
        Node n = new Node(x, y, label);
        n.isStart = isStart;
        n.isEnd = isEnd;
        return n;
    }

    /**
     * Checks if this node equals another node by comparing the x- and
     * y-coordinates.
     *
     * @param n The node to compare with
     * @return True if the nodes are equal, false otherwise
     */
    public boolean equals(Node n) {
        if (n.x == x && n.y == y) {
            return true;
        }
        return false;
    }

    /***********
     *
     * The methods below are only used by the GUI.
     * Leave them as they are now.
     *
     ***********/

    /**
     * Used by the GUI only.
     *
     * @return draw coordinate
     */
    public int getDrawX() {
        int c = x * 10;
        return c - 8;
    }

    /**
     * Used by the GUI only.
     *
     * @return draw coordinate
     */
    public int getDrawY() {
        int c = y * 10;
        return c - 6;
    }

    /**
     * Used by the GUI only.
     *
     * @return draw coordinate
     */
    public int getDrawLabelX() {
        int c = x * 10;
        return c - label.length() * 4 + 2;
    }

    /**
     * Used by the GUI only.
     *
     * @return draw coordinate
     */
    public int getDrawLabelY() {
        int c = y * 10;
        return c + 6;
    }
}
