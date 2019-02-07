package uk.ac.newcastle.team22.usb.navigation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Vincent
 * @version 1.0
 */
public class Node implements Cloneable {
    private int nodeID;
    private List<Edge> adjacent = new ArrayList<Edge>();

    public Node(int nodeID) {
        this.nodeID = nodeID;
    }

    private Node(int nodeID, List<Edge> adjacent) {
        this.nodeID = nodeID;
        this.adjacent = new ArrayList<>(adjacent);
    }

    /**
     * Return Node identifier
     * @return unique ID of this Node
     */
    public int getNodeID() {
        return nodeID;
    }

    public void addAdjacent(Node adj, int weight, List<Direction> directions, List<Integer> distances, boolean cardLocked, boolean accessible) {
        this.adjacent.add(new Edge(this, adj, weight, directions, distances, cardLocked, accessible));
    }

    /**
     * Get adjacent Edges of this Node with defensive copying
     * @return
     */
    public List<Edge> getAdjacents() {
        return new ArrayList<Edge>(adjacent);
    }

    /**
     * @return Logically equal copy of this Node
     */
    @Override
    public Node clone() {
        return new Node(this.nodeID, this.adjacent);
    }

    /**
     * Logical equality checking for Node objects, falls back to superclass for other object types
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if(obj.getClass().equals(Node.class)) {
            if(this.nodeID == ((Node)obj).getNodeID()) {
                if(this.adjacent.equals(((Node) obj).getAdjacents()));
                    return true;
            }
        }
        return super.equals(obj);
    }
}
