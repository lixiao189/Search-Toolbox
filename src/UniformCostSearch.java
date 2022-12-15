import java.util.Vector;

/**
 * This is the implementation of the Uniform-cost search algorithm for
 * pathfinding.
 *
 * @author Johan Hagelbäck
 */
public class UniformCostSearch extends SearchMethod {
    /**
     * Initializes a new Uniform-cost search.
     */
    public UniformCostSearch() {
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
        //Find the next node to visit = the node with the shortest path so far
        Node n = getNextToVisit();
        map.setLinkAsVisited(n);
        //Move the visited node to the closed list
        closed.add(n);
        //Increase the visited counter
        noVisited++;

        //Check if we are finished
        if (n.equals(end)) {
            //Goal node reached!
            path = reconstructPath(n);
            return true;
        } else {
            //Not finished yet. Keep iterating.

            //Find the nodes that are connected to the current node n
            Vector<Node> connected = getConnectedNodes(n);
            for (Node c : connected) {
                //Set parent reference
                c.parent = n;
                //Calculate the actual cost (path length) of going from
                //the start node to the current node c.
                calculateActualCost(c);

                //Check if the node already is in the open or closed lists.
                //If it is, we have found multiple paths from the start node to 
                //the current node c. Node c must be updated to contain the
                //shortest of the multiple paths.
                Node no = findInOpen(c);
                if (no != null) {
                    //The actual cost of the current node c is lower than the actual cost
                    //for the previous copy of the node.
                    if (c.actual < no.actual) {
                        //Remove the previous copy and add the new node c.
                        open.remove(no);
                        open.add(c);
                    }
                }
                Node nc = findInClosed(c);
                if (nc != null) {
                    //The actual cost of the current node c is lower than the actual cost
                    //for the previous copy of the node.
                    if (c.actual < nc.actual) {
                        //Remove the previous copy and add the new node c.
                        closed.remove(nc);
                        open.add(c);
                    }
                }
                //Node is not found in the open or closed lists, so we add it.
                if (no == null && nc == null) {
                    open.add(c);
                }
            }
        }

        return false;
    }

    /**
     * Returns the best node in the open list. The best node is the node with
     * the shortest actual cost of getting from the start node to the current node.
     *
     * @return The best node in the open list
     */
    private Node getNextToVisit() {
        //Pick first node...
        Node bestNode = open.elementAt(0);
        //... and calculate its actual cost
        double bestA = bestNode.actual;

        //Find the best node in the list
        for (Node n : open) {
            //Calculate actual cost for current node n
            double currentA = n.actual;
            if (currentA < bestA) {
                //We have a new best node
                bestA = currentA;
                bestNode = n;
            }
        }

        //Remove the best node from the open list (since we visit/expand it)
        open.remove(bestNode);

        return bestNode;
    }

    /**
     * Calculate the Euclidean distance from node n to the end node
     * using Pythagoras theorem.
     *
     * @param n The node to calculate distance for
     * @return The Euclidean distance
     */
    private double getHeuristic(Node n) {
        double sqX = Math.pow(end.x - n.x, 2);
        double sqY = Math.pow(end.y - n.y, 2);
        double euclidean = Math.sqrt(sqX + sqY);
        return euclidean;
    }

    /**
     * Calculates the actual cost (path length) of going from the start node
     * to the specified node.
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
