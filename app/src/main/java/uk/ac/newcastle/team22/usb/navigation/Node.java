package uk.ac.newcastle.team22.usb.navigation;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.ac.newcastle.team22.usb.coreUSB.USBManager;
import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;

/**
 * A class that represents a node used for navigation in the Urban Sciences Building.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public class Node implements FirestoreConstructable<Node> {
    private int nodeIdentifier;
    private int floorNumber;
    private boolean isTourNode = false;
    private int imageIdentifier;
    private String name;
    private String description;
    private List<Edge> edges = new ArrayList<Edge>();

    /**
     * Public constructor.
     * @param nodeIdentifier Identifier of node in <pre>USB.navigationNodes</>.
     * @param floorNumber The floor this node resides on.
     * @param edges A list of edges adjacent to this node.
     */
    public Node(int nodeIdentifier, int floorNumber, List<Edge> edges) {
        this.nodeIdentifier = nodeIdentifier;
        this.floorNumber = floorNumber;
        this.edges = edges;
    }

    /** Empty constructor (for Firebase). */
    public Node() {}

    /**
     * @param firestoreDictionary The Firestore document dictionary.
     * @param documentIdentifier The identifier of the Firestore document, used as the <pre>nodeIdentifier</pre>.
     * @return A node from database data.
     * @throws FirestoreConstructable.InitialisationFailed
     */
    @Override
    public Node initFromFirebase(Map<String, Object> firestoreDictionary, String documentIdentifier) throws FirestoreConstructable.InitialisationFailed {
        // Check if this node should be a tour node.
        if (firestoreDictionary.get("description") != null) {
            this.isTourNode = true;
            this.description = (String) firestoreDictionary.get("description");
            this.imageIdentifier = ((Number) firestoreDictionary.get("imageID")).intValue();
            this.name = (String) firestoreDictionary.get("name");
        }

        this.nodeIdentifier = Integer.parseInt(documentIdentifier);
        this.floorNumber = ((Number) firestoreDictionary.get("floor")).intValue();

        List<Map<String, Object>> edges = (ArrayList<Map<String, Object>>) firestoreDictionary.get("edges");

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
     * @return Floor number which the node resides on.
     */
    public boolean isTourNode() {
        return this.isTourNode;
    }

    /**
     * @return Long description of this Tour Node's location.
     */
    public String getDescription() {
        if (!this.isTourNode) {throw new IllegalArgumentException("Non TourNode is being treated as TourNode.");}
        return description;
    }

    /**
     * @return Image name of this Tour Node's location.
     */
    public int getImageIdentifier() {
        if (!this.isTourNode) {throw new IllegalArgumentException("Non TourNode is being treated as TourNode.");}
        return imageIdentifier;
    }

    /**
     * @return Tour name of this Tour Node's location.
     */
    public String getName() {
        if (!this.isTourNode) {throw new IllegalArgumentException("Non TourNode is being treated as TourNode.");}
        return name;
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
            if (this.nodeIdentifier == that.getNodeIdentifier() && this.floorNumber == that.getFloorNumber() && this.edges.equals(that.getEdges()) && this.isTourNode == that.isTourNode) {
                return true;
            }
        }
        return super.equals(obj);
    }
}
