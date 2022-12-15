import java.util.Vector;

/**
 * This is the implementation of the Breadth-First Search algorithm for
 * pathfinding.
 *
 * @author Johan Hagelbäck
 */
public class BreadthFirst extends SearchMethod {
    /**
     * Initializes a new Breadth-First Search.
     */
    public BreadthFirst() {
        //Must be called to reset all variables and
        //data structures needed for the search.
        init();
    }

    /**
     * Finds a path from start to end node.
     *
     * @return True when the search is ready, false if not ready.
     */
    public boolean step() {
        //Visit (expand) the first node in the open list
        Node n = open.remove(0);
        map.setLinkAsVisited(n);
        //Move the visited node to the closed list
        closed.add(n);
        //Increase the visited counter
        noVisited++;

        //Check if we are finished = current node equals end node
        if (n.equals(end)) {
            //Goal node reached!
            path = reconstructPath(n);
            return true;
        } else {
            //Not finished yet. Keep iterating.

            //Find the nodes that are connected to the current node n
            Vector<Node> connected = getConnectedNodes(n);
            for (Node c : connected) {
                //If a node is not in the open or closed lists, add it
                //to the open list
                if (!isInOpenOrClosed(c)) {
                    //Set the parent reference
                    c.parent = n;
                    //Add last in the open list (FIFO)
                    open.add(c);
                }
            }
        }

        return false;
    }
}
