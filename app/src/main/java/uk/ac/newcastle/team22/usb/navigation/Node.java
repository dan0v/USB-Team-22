package uk.ac.newcastle.team22.usb.navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.ac.newcastle.team22.usb.coreUSB.USBManager;
import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;

/**
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
     * @param floorNumber
     */
    public Node(int nodeIdentifier, int floorNumber) {
        this.nodeIdentifier = nodeIdentifier;
        this.floorNumber = floorNumber;
        //add this Node to the shared map of Nodes for access during Navigation
        USBManager.sharedNodes.put(nodeIdentifier, this);
    }

    @Override
    public Node initFromFirebase(Map<String, Object> firestoreDictionary, String documentIdentifier) throws FirestoreConstructable.InitialisationFailed {
        int firestoreFloorNumber = (int) firestoreDictionary.get("floor");
        List<Map<String, Object>> firestoreEdges = (ArrayList<Map<String, Object>>) firestoreDictionary.get("edges");

        this.nodeIdentifier = Integer.parseInt(documentIdentifier);
        this.floorNumber = firestoreFloorNumber;

        if (firestoreEdges != null) {
            for (Map<String, Object> edgeData : firestoreEdges) {
                Edge edge = new Edge(this, edgeData);
                this.addEdge(edge);
            }
        }
        else {
            throw new FirestoreConstructable.InitialisationFailed("Node could not be initialised - missing Edges");
        }

        //add this Node to the shared map of Nodes for access during Navigation
        USBManager.sharedNodes.put(nodeIdentifier, this);

        return this;
    }

    /**
     * @return Unique ID of this Node.
     */
    public int getNodeIdentifier() {
        return this.nodeIdentifier;
    }

    /**
     * @return Floor number node resides on.
     */
    public int getFloorNumber() {
        return this.floorNumber;
    }

    /**
     * Add an Edge to this Node.
     * @param edge Edge to add to this Node's List of Edges.
     */
    public void addEdge(Edge edge) {
        this.edges.add(edge);
    }

    /**
     * @return List of Edges.
     */
    public List<Edge> getEdges() {
        return edges;
    }

    /**
     * Logical equality checking for Node objects, falls back to superclass for other object types.
     * @param obj Object to compare to <pre>this</pre>.
     * @return True if Node objects are logically equivalent, or have same hash, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj.getClass().equals(Node.class)) {
            Node that = (Node)obj;
            if(this.nodeIdentifier == that.getNodeIdentifier() && this.floorNumber == that.getFloorNumber() && this.edges.equals(that.getEdges())) {
                    return true;
            }
        }
        return super.equals(obj);
    }
}
