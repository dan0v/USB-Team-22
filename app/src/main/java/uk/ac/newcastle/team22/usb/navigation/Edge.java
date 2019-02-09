package uk.ac.newcastle.team22.usb.navigation;

import java.util.ArrayList;
import java.util.List;

import uk.ac.newcastle.team22.usb.coreUSB.USBManager;

/**
 * A class that represents an edge between two {@link Node} in the Urban Sciences Building.
 * See {@link Node} for more information on nodes.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public class Edge {
    private final int originID;
    private final int destinationID;
    public final int weight;
    public final List<Direction> directions;
    public final List<Integer> distances;
    public final boolean cardLocked;
    public final boolean accessible;


    /**
     * Construct an Edge between nodes.
     * @param originID Identifier of origin Node of this Edge.
     * @param destinationID Identifier of destination Node of this Edge.
     * @param weight Total weight of the path this Edge represents.
     * @param directions List of Direction - Copy is made.
     * @param distances List of distances for each direction - Copy is made.
     * @param cardLocked boolean whether card access is required.
     * @param accessible boolean whether this edge meets accessibility needs.
     */
    public Edge(int originID, int destinationID, int weight, List<Direction> directions, List<Integer> distances, boolean cardLocked, boolean accessible) {
        this.originID = originID;
        this.destinationID = destinationID;
        this.weight = weight;
        this.directions = new ArrayList<Direction>(directions);
        this.distances = new ArrayList<Integer>(distances);
        this.cardLocked = cardLocked;
        this.accessible = accessible;
        USBManager.sharedNodes.get(originID).addEdge(this);
    }

    /**
     * @return Node with origin ID from <pre>USBManager.sharedNodes</pre>.
     */
    public Node getOrigin() {
        return USBManager.sharedNodes.get(originID);
    }

    /**
     * @return Node with destination ID from <pre>USBManager.sharedNodes</pre>.
     */
    public Node getDestination() {
        return USBManager.sharedNodes.get(destinationID);
    }

    /**
     * Logical equality checking for Edges, falls back to superclass for unknown object types.
     * @param obj Object to compare to <pre>this</pre>.
     * @return true if Edge objects are logically equivalent, or have same hash, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj.getClass().equals(Edge.class)) {
            Edge that = (Edge)obj;
            if(this.getOrigin().equals(that.getOrigin()) && this.getDestination().equals(that.getDestination()) && this.weight == that.weight && this.directions.equals(that.directions) && this.distances.equals(that.distances) && this.cardLocked == that.cardLocked && this.accessible == that.accessible) {
                return true;
            }
        }
        return super.equals(obj);
    }
}
