import java.util.Vector;

/**
 * This is where you implement the A-star search algorithm.
 * It currently has a Depth-First search implementation.
 *
 * @author Li Xiao
 */
public class Astar extends SearchMethod {
    /**
     * Initializes a new A-star search.
     */
    public Astar() {
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
        //Visit (expand) the best node in the open list
        Node n = getNextToVisit();
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
                    //Add first in the open list (LIFO)
                    open.add(0, c);
                }
            }
        }

        return false;
    }

    /**
     * Returns the best node in the open list. This is where you implement
     * logic for finding the best node in the open list.
     *
     * @return The best node in the open list
     */
    private Node getNextToVisit() {
        //Pick first node...
        Node bestNode = open.elementAt(0);

        return bestNode;
    }

    /**
     * Calculates the actual cost (path length) of going from the start node
     * to the specified node. Use it when calculating the best node in the
     * open list.
     *
     * @param n The current node
     */
    private void calculateActualCost(Node n) {
        //Reconstruct the path by traversing the parent references from the
        //current node to the start node
        Path path = reconstructPath(n);
        //Set the actual cost to the length of the reconstructed path
        n.actual = path.getLength();
    }
}
