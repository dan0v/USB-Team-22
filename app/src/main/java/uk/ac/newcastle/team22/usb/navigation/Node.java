package uk.ac.newcastle.team22.usb.navigation;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.ac.newcastle.team22.usb.R;
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
     * @return Image of this Tour Node's location.
     */
    public int getImage() {
        if (!this.isTourNode) {throw new IllegalArgumentException("Non TourNode is being treated as TourNode.");}

        switch (imageIdentifier) {
            case 1:
                return R.drawable.tour_image_1;
            case 2:
                return R.drawable.tour_image_2;
            case 3:
                return R.drawable.tour_image_3;
            case 4:
                return R.drawable.tour_image_4;
            case 5:
                return R.drawable.tour_image_5;
            case 6:
                return R.drawable.tour_image_6;
            case 7:
                return R.drawable.tour_image_7;
            case 8:
                return R.drawable.tour_image_8;
            case 9:
                return R.drawable.tour_image_9;
            case 10:
                return R.drawable.tour_image_10;
            case 11:
                return R.drawable.tour_image_11;
            case 12:
                return R.drawable.tour_image_12;
            case 13:
                return R.drawable.tour_image_13;
            case 14:
                return R.drawable.tour_image_14;
            case 15:
                return R.drawable.tour_image_15;
            case 16:
                return R.drawable.tour_image_16;
            case 17:
                return R.drawable.tour_image_17;
            case 18:
                return R.drawable.tour_image_18;
            default:
                return R.drawable.usb_hero;
        }
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
