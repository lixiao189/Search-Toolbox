/**
 * This class represents a link between two nodes in the map.
 *
 * @author Johan Hagelbäck
 */
public class Link {
    /**
     * First node
     */
    public Node a;
    /**
     * Second node
     */
    public Node b;

    /**
     * Link is shown as black in map (not visited). This is default.
     */
    public static int NONE = 0;

    /**
     * Link is shown blue in the map (node is visited).
     */
    public static int VISITED = 1;

    /**
     * Link is shown as red in the map (is in the found path).
     */
    public static int INPATH = 2;

    /**
     * Determines how the link is shown in the map GUI.
     */
    public int visibility = NONE;

    /**
     * Creates a new link.
     *
     * @param a First node
     * @param b Second node
     */
    public Link(Node a, Node b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Checks if this link equals another link by comparing the two
     * nodes connected by the link.
     *
     * @param l The other link to compare with
     * @return True if the links are equal, false otherwise
     */
    public boolean equals(Link l) {
        if (l.a.equals(a) && l.b.equals(b)) {
            return true;
        }
        return false;
    }

    /**
     * Returns the label for this link. A link is labelled with
     * the label of both nodes separated by -.
     *
     * @return The label for this link
     */
    public String getLabel() {
        return a.label + "-" + b.label;
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
    public int getDrawX1() {
        return a.getDrawX() + 8;
    }

    /**
     * Used by the GUI only.
     *
     * @return draw coordinate
     */
    public int getDrawX2() {
        return b.getDrawX() + 8;
    }

    /**
     * Used by the GUI only.
     *
     * @return draw coordinate
     */
    public int getDrawY1() {
        return a.getDrawY() + 8;
    }

    /**
     * Used by the GUI only.
     *
     * @return draw coordinate
     */
    public int getDrawY2() {
        return b.getDrawY() + 8;
    }
}
