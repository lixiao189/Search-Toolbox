import javax.swing.*;
import java.util.Vector;

/**
 * This class holds the map to search for a path in. A map is a set of nodes
 * connected by links.
 *
 * @author Johan Hagelbäck
 */
public class Map {
    /**
     * The nodes in the map
     */
    private Vector<Node> nodes;
    /**
     * The links in the map
     */
    private Vector<Link> links;
    /**
     * This is a singleton class
     */
    private static Map instance;
    /**
     * The filename of the map to load
     */
    private String mapID = "1";

    /**
     * Singleton class.
     *
     * @return The class instance
     */
    public static Map getInstance() {
        if (instance == null) {
            instance = new Map();
        }
        return instance;
    }

    /**
     * Private constructor (singleton class)
     */
    private Map() {
        nodes = new Vector<Node>();
        links = new Vector<Link>();
        readMap();
    }

    /**
     * Returns the nodes in this map.
     *
     * @return A list of nodes
     */
    public Vector<Node> getNodes() {
        return nodes;
    }

    /**
     * Shows the link as visited in the GUI. The link is the one connecting the specified
     * node and its parent.
     *
     * @param n The node
     */
    public void setLinkAsVisited(Node n) {
        Node a = n.parent;
        if (a == null) return;
        Node b = n;

        for (Link l : links) {
            if (l.a.equals(a) && l.b.equals(b)) {
                l.visibility = Link.VISITED;
            }
        }
    }

    /**
     * Returns the links in this map.
     *
     * @return A list of links
     */
    public Vector<Link> getLinks() {
        return links;
    }

    /**
     * Resets all links from being shown.
     */
    public void resetLinks() {
        for (Link l : links) {
            l.visibility = Link.NONE;
        }
    }

    /**
     * Resets a map. This removes the path from being shown in the GUI and
     * clears all parent references for nodes.
     */
    public void reset() {
        resetLinks();

        for (Node n : nodes) {
            n.parent = null;
        }
    }

    /**
     * Loads a map.
     *
     * @param mapID The map to load
     */
    public void load(String mapID) {
        this.mapID = mapID;
        readMap();
    }

    /**
     * Reloads the current map.
     */
    public void reload() {
        readMap();
    }

    /**
     * Reads a new map.
     */
    public void readMap() {
        try {
            nodes = new Vector<Node>();
            links = new Vector<Link>();

            String[] cont = null;
            if (mapID.equals("1")) cont = Map1.cont;
            if (mapID.equals("2")) cont = Map2.cont;

            if (cont != null) {
                for (String l : cont) {
                    parseLine(l);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Parses a line read from a map file.
     *
     * @param line The line to parse
     */
    public void parseLine(String line) {
        try {
            String[] tokens = line.split(" ");
            if (line.startsWith("Node") && tokens.length == 4) {
                String label = tokens[1];
                int x = Integer.parseInt(tokens[2]);
                int y = Integer.parseInt(tokens[3]);
                addNode(new Node(x, y, label));
            }
            if (line.startsWith("Link") && tokens.length == 3) {
                Node a = findNode(tokens[1]);
                Node b = findNode(tokens[2]);
                if (a != null && b != null) {
                    addLink(new Link(a, b));
                    addLink(new Link(b, a));
                }
            }
            if (line.startsWith("Start") && tokens.length == 2) {
                Node n = findNode(tokens[1]);
                if (n != null) {
                    n.isStart = true;
                }
            }
            if (line.startsWith("End") && tokens.length == 2) {
                Node n = findNode(tokens[1]);
                if (n != null) {
                    n.isEnd = true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Dumps the map to code. Used when creating new maps in the GUI.
     */
    public void dumpMap() {
        for (Node n : nodes) {
            System.out.println("\t\t\"Node " + n.label + " " + n.x + " " + n.y + "\",");
            if (n.isStart) {
                System.out.println("\t\t\"Start " + n.label + "\",");
            }
            if (n.isEnd) {
                System.out.println("\t\t\"End " + n.label + "\",");
            }
        }

        String pl1 = "";
        String pl2 = "";
        for (Link l : links) {
            String l1 = l.a.label;
            String l2 = l.b.label;
            boolean show = true;
            if (l1.equals(pl1) && l2.equals(pl2)) show = false;
            if (l1.equals(pl2) && l2.equals(pl1)) show = false;
            if (show) {
                System.out.println("\t\t\"Link " + l1 + " " + l2 + "\",");

                pl1 = l1;
                pl2 = l2;
            }
        }
    }

    /**
     * Adds a new node. Used when load the map from file.
     *
     * @param n The node to add
     */
    private void addNode(Node n) {
        boolean found = false;
        for (Node nn : nodes) {
            if (nn.equals(n)) {
                found = true;
                break;
            }
        }

        if (!found) {
            nodes.add(n);
        }
    }

    /**
     * Creates a new unique label (increasing integer number) used for new nodes.
     *
     * @return The new unique label
     */
    public String getNewNodeLabel() {
        if (nodes.size() == 0) {
            return "1";
        }

        int no = -1;
        for (Node n : nodes) {
            int cl = -1;
            try {
                cl = Integer.parseInt(n.label);
            } catch (Exception ex) {
                cl = -1;
            }
            if (cl > no) {
                no = cl;
            }
        }

        no++;

        return "" + no;
    }

    /**
     * Adds a new link. Used when load the map from file.
     *
     * @param l The link to add
     */
    private void addLink(Link l) {
        boolean found = false;
        for (Link ll : links) {
            if (ll.equals(l)) {
                found = true;
                break;
            }
        }

        if (!found) {
            links.add(l);
        }
    }

    /**
     * Removes a node from the map. Used by the GUI when the user changes the map.
     *
     * @param label The label of the node to remove
     * @return True if the node was successfully removed, falst otherwise
     */
    public boolean removeNode(String label) {
        boolean ok = false;

        for (int i = 0; i < links.size(); i++) {
            Link l = links.elementAt(i);
            if (l.a.label.equalsIgnoreCase(label)) {
                links.remove(i);
                ok = true;
                i--;
            }
            if (l.b.label.equalsIgnoreCase(label)) {
                links.remove(i);
                ok = true;
                i--;
            }
        }

        for (int i = 0; i < nodes.size(); i++) {
            Node n = nodes.elementAt(i);
            if (n.label.equalsIgnoreCase(label)) {
                nodes.remove(i);
                ok = true;
                i--;
            }
        }
        return ok;
    }

    /**
     * Adds a new node to the map. Used by the GUI when the user changes the map.
     *
     * @param x     X-coordinate
     * @param y     Y-coordinate
     * @param label Label for the node
     * @return True if the node was successfully added, false otherwise
     */
    public boolean addNode(int x, int y, String label) {
        for (Node n : nodes) {
            if (n.label.equals(label)) {
                JOptionPane.showMessageDialog(null, "A node with label '" + label + "' already exists", "Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        nodes.add(new Node(x, y, label));
        return true;
    }

    /**
     * Adds a new link between two nodes. Used by the GUI when the user changes the map.
     *
     * @param node1 First node
     * @param node2 Second node
     * @return True if the link was successfully added, false otherwise
     */
    public boolean addLink(String node1, String node2) {
        Node n1 = findNode(node1);
        Node n2 = findNode(node2);
        if (n1 != null && n2 != null) {
            links.add(new Link(n1, n2));
            links.add(new Link(n2, n1));
        } else {
            if (n1 == null)
                JOptionPane.showMessageDialog(null, "Unable to find node with label '" + node1 + "'", "Error", JOptionPane.WARNING_MESSAGE);
            if (n2 == null)
                JOptionPane.showMessageDialog(null, "Unable to find node with label '" + node2 + "'", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Removes a link from the map. Used by the GUI when the user changes the map.
     *
     * @param label The label of the link to remove
     * @return True if the link was successfully removed, false otherwise
     */
    public boolean removeLink(String label) {
        String l1 = label;
        String[] t = l1.split("-");
        String l2 = t[1] + "-" + t[0];

        boolean ok = false;

        for (int i = 0; i < links.size(); i++) {
            Link l = links.elementAt(i);
            if (l.getLabel().equalsIgnoreCase(l1) || l.getLabel().equalsIgnoreCase(l2)) {
                links.remove(i);
                ok = true;
                i--;
            }
        }
        return ok;
    }

    /**
     * Finds the node with the specified label.
     *
     * @param label The label of the node
     * @return The node, or null if not found
     */
    private Node findNode(String label) {
        for (Node n : nodes) {
            if (n.label.equalsIgnoreCase(label)) {
                return n;
            }
        }
        return null;
    }

    /**
     * Finds the link with the specified label.
     *
     * @param label The label of the link
     * @return The link, or null if not found
     */
    public Link findLink(String label) {
        for (Link l : links) {
            if (l.getLabel().equalsIgnoreCase(label)) {
                return l;
            }
        }
        return null;
    }

    /**
     * Returns the start node in the map.
     *
     * @return The start node
     */
    public Node getStartNode() {
        for (Node n : nodes) {
            if (n.isStart) return n;
        }
        return null;
    }

    /**
     * Changes the start node in the map. Used by the GUI when the user changes the map.
     *
     * @param label The label of the new start node
     */
    public void setAsStartNode(String label) {
        for (Node n : nodes) {
            n.isStart = false;
            if (n.label.equals(label)) {
                n.isStart = true;
            }
        }
    }

    /**
     * Returns the end node in the map.
     *
     * @return The end node
     */
    public Node getEndNode() {
        for (Node n : nodes) {
            if (n.isEnd) return n;
        }
        return null;
    }

    /**
     * Changes the end node in the map. Used by the GUI when the user changes the map.
     *
     * @param label The end of the new start node
     */
    public void setAsEndNode(String label) {
        for (Node n : nodes) {
            n.isEnd = false;
            if (n.label.equals(label)) {
                n.isEnd = true;
            }
        }
    }

    /**
     * Returns the link between two nodes.
     *
     * @param a First node
     * @param b Second node
     * @return The link between the nodes, or null if no link was found
     */
    public Link getLink(Node a, Node b) {
        for (Link l : links) {
            if (l.a == a && l.b == b) {
                return l;
            }
            if (l.b == a && l.a == b) {
                return l;
            }
        }
        return null;
    }

    /**
     * Returns all the links going to and from the specified node.
     *
     * @param a The node
     * @return A list of links
     */
    public Vector<Link> getLinks(Node a) {
        Vector<Link> c = new Vector<Link>();
        for (Link l : links) {
            if (l.a == a || l.b == a) {
                c.add(l);
            }
        }
        return c;
    }
}
