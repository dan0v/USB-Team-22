package uk.ac.newcastle.team22.usb.navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.ac.newcastle.team22.usb.coreUSB.USBManager;
import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;

/**
 * A class that represents a Node used for navigation in the Urban Sciences Building.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public class Node implements FirestoreConstructable<Node> {
    private int nodeIdentifier;
    private int floorNumber;
    private List<Edge> edges = new ArrayList<Edge>();

    /**
     * Public constructor.
     * @param nodeIdentifier Identifier of Node in <pre>USBManager.sharedNodes</>.
     * @param floorNumber The floor this Node resides on.
     * @param edges A List of Edges adjacent to this Node.
     */
    public Node(int nodeIdentifier, int floorNumber, List<Edge> edges) {
        this.nodeIdentifier = nodeIdentifier;
        this.floorNumber = floorNumber;
        this.edges = edges;
    }

    /** Empty constructor. */
    public Node() {}

    @Override
    public Node initFromFirebase(Map<String, Object> firestoreDictionary, String documentIdentifier) throws FirestoreConstructable.InitialisationFailed {
        int floorNumber = ((Long) firestoreDictionary.get("floor")).intValue();
        List<Map<String, Object>> edges = (ArrayList<Map<String, Object>>) firestoreDictionary.get("edges");

        this.nodeIdentifier = Integer.parseInt(documentIdentifier);
        this.floorNumber = floorNumber;

        // Initialise node's edges.
        if (edges != null) {
            for (Map<String, Object> edgeData : edges) {
                Edge edge = new Edge(this, edgeData);
                this.edges.add(edge);
            }
        } else {
            throw new FirestoreConstructable.InitialisationFailed("Node could not be initialised - missing Edges");
        }

        return this;
    }

    /**
     * @return Unique identifier of the node.
     */
    public int getNodeIdentifier() {
        return this.nodeIdentifier;
    }

    /**
     * @return Floor number which the node resides on.
     */
    public int getFloorNumber() {
        return this.floorNumber;
    }

    /**
     * @return List of edges.
     */
    public List<Edge> getEdges() {
        return this.edges;
    }

    /**
     * Logical equality checking for Node objects, falls back to superclass for other object types.
     * @param obj Object to compare to <pre>this</pre>.
     * @return True if Nodes are logically equivalent, or Objects have the same hash, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(Node.class)) {
            Node that = (Node) obj;
            if (this.nodeIdentifier == that.getNodeIdentifier() && this.floorNumber == that.getFloorNumber() && this.edges.equals(that.getEdges())) {
                return true;
            }
        }
        return super.equals(obj);
    }
}
