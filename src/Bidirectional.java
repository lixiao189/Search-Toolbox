import java.util.Vector;

/**
 * This is the implementation of the Bi-directional Search algorithm for
 * pathfinding.
 *
 * @author Johan Hagelbäck
 */
public class Bidirectional extends SearchMethod {
    private Vector<Node> open_fwd;
    private Vector<Node> closed_fwd;
    private Vector<Node> open_bwd;
    private Vector<Node> closed_bwd;

    /**
     * Initializes a new Bi-directional Search.
     */
    public Bidirectional() {
        //Must be called to reset all variables and
        //data structures needed for the search.
        init();

        open_fwd = new Vector<Node>();
        closed_fwd = new Vector<Node>();
        open_bwd = new Vector<Node>();
        closed_bwd = new Vector<Node>();

        //Add start nodes
        open_fwd.add(start.clone());
        open_bwd.add(end.clone());
    }

    /**
     * Finds a path from start to end node.
     *
     * @return True when the search is ready, false if not ready.
     */
    public boolean step() {
        step(open_fwd, closed_fwd);
        step(open_bwd, closed_bwd);

        //Check if the searches have met
        for (Node n_fwd : closed_fwd) {
            for (Node n_bwd : closed_bwd) {
                if (n_fwd.equals(n_bwd)) {
                    //The searches have met!
                    //Generate the path and stop the search
                    Path path_fwd = reconstructPath(n_fwd);
                    Path path_bwd = reconstructPath(n_bwd);
                    path_fwd.showPathLinks();
                    path_bwd.showPathLinks();

                    //Merge the two paths
                    path = path_fwd;
                    for (int i = path_bwd.path.size() - 2; i >= 0; i--) {
                        path.addLast(path_bwd.path.elementAt(i));
                    }
                    //Recalculate the path length since it won't
                    //be correct after the merge.
                    path.recalculateLength();

                    return true;
                }
            }
        }

        return false;
    }

    private void step(Vector<Node> cOpen, Vector<Node> cClosed) {
        //Step the forward search
        Node n = cOpen.remove(0);
        map.setLinkAsVisited(n);
        //Move the visited node to the closed list
        cClosed.add(n);
        //Increase the visited counter
        noVisited++;

        //Find the nodes that are connected to the current node n
        Vector<Node> connected = getConnectedNodes(n);
        for (Node c : connected) {
            //If a node is not in the open or closed lists, add it
            //to the open list
            if (!isInOpenOrClosed(c, cOpen, cClosed)) {
                //Set the parent reference
                c.parent = n;
                //Add last in the open list (FIFO)
                cOpen.add(c);
            }
        }
    }

    private boolean isInOpenOrClosed(Node n, Vector<Node> cOpen, Vector<Node> cClosed) {
        for (Node c : cOpen) {
            if (c.equals(n)) return true;
        }

        for (Node c : cClosed) {
            if (c.equals(n)) return true;
        }

        return false;
    }
}
