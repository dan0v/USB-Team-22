package uk.ac.newcastle.team22.usb.navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A class that represents an edge between two {@link Node} in the Urban Sciences Building.
 * See {@link Node} for more information on nodes.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public class Edge {
    public final Node origin;
    public final Node destination;
    public final int weight;
    public final List<Direction> directions;
    public final List<Integer> distances;
    public final boolean cardLocked;
    public final boolean accessible;

    /** The identifier of the destination node. */
    private int destinationNodeIdentifier;

    /**
     * @return The destination node.
     */
    public Node getDestinationNode() {
        List<Node> allNodes = new ArrayList<Node>();
        for (Node node : allNodes) {
            if (node.getNodeID() == destinationNodeIdentifier) {
                return node;
            }
        }
        return null;
    }

    public Edge(String firestoreDictionary, Node origin) {
        this.origin = origin;
    }

    /**
     * Construct an Edge between nodes
     * @param origin Node
     * @param weight total weight of path
     * @param directions List of Direction - Copy is made
     * @param distances List of distances for each direction - Copy is made
     * @param cardLocked boolean whether card access is required
     * @param accessible boolean whether this edge meets accessibility needs
     */
    public Edge(Node origin, int weight, List<Direction> directions, List<Integer> distances, boolean cardLocked, boolean accessible) {
        this.origin = origin;
        this.destination = destination;
        this.weight = weight;
        this.directions = new ArrayList<Direction>(directions);
        this.distances = new ArrayList<Integer>(distances);
        this.cardLocked = cardLocked;
        this.accessible = accessible;
    }

    /**
     * Logical equality checking for Edges, falls back to superclass for other object types
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if(obj.getClass().equals(Edge.class)) {
            Edge that = (Edge)obj;
            if(this.origin.equals(that.origin) && this.destination.equals(that.destination) && this.weight == that.weight && this.directions.equals(that.directions) && this.distances.equals(that.distances) && this.cardLocked == that.cardLocked && this.accessible == that.accessible) {
                return true;
            }
        }
        return super.equals(obj);
    }
}
