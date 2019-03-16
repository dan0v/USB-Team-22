package uk.ac.newcastle.team22.usb.navigation;

import android.util.Log;

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

    /** Constructor from TourNode. */
    protected Node(int nodeIdentifier, int floorNumber) {
        this.nodeIdentifier = nodeIdentifier;
        this.floorNumber = floorNumber;
    }

    /** Empty constructor. */
    public Node() {}

    /**
     * Creates and returns a Node or TourNode depending on dictionary contents.
     * @param firestoreDictionary The Firestore document dictionary.
     * @param documentIdentifier The identifier of the Firestore document, used as the nodeIdentifier.
     * @return
     * @throws FirestoreConstructable.InitialisationFailed
     */
    @Override
    public Node initFromFirebase(Map<String, Object> firestoreDictionary, String documentIdentifier) throws FirestoreConstructable.InitialisationFailed {
        // Check if this Node should be a TourNode.
        if (firestoreDictionary.get("description") != null) {
            String description = (String) firestoreDictionary.get("description");
            int floorNumber = ((Number) firestoreDictionary.get("floor")).intValue();
            String imageIdentifier = (String) firestoreDictionary.get("imageID");
            String nodeName = (String) firestoreDictionary.get("name");
            List<Map<String, Object>> edges = (ArrayList<Map<String, Object>>) firestoreDictionary.get("edges");
            int nodeIdentifier = Integer.parseInt(documentIdentifier);

            Node initialisedTourNode = new TourNode(nodeIdentifier, floorNumber, description, imageIdentifier, nodeName);

            for (Map<String, Object> edgeData : edges) {
                Edge edge = new Edge(initialisedTourNode, edgeData);
                initialisedTourNode.getEdges().add(edge);
            }
            String msg = String.format("Created tour node: %s", nodeIdentifier);
            Log.d("Navigation",msg);

            return initialisedTourNode;
        }

        // This Node is not a TourNode, so initialise it normally.

        int floorNumber = ((Number) firestoreDictionary.get("floor")).intValue();
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
        String msg = String.format("Created normal node: %s", nodeIdentifier);
        Log.d("Navigation",msg);

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
