package uk.ac.newcastle.team22.usb.navigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import uk.ac.newcastle.team22.usb.coreUSB.Resource;
import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;

/**
 * A class that represents a node in the Urban Sciences Building.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public class Node implements Cloneable, FirestoreConstructable<Node> {

    /** The unique identifier of the node. */
    private int nodeIdentifier;

    /** The adjacent edges to the node. */
    private List<Edge> adjacentEdges = new ArrayList<>();

    @Override
    public Node initFromFirebase(Map<String, Object> firestoreDictionary, String documentIdentifier) {
        //List<> edges = (ArrayList<>) firestoreDictionary.get("edges");
        this.nodeIdentifier = Integer.parseInt(documentIdentifier);
        return this;
    }

    public Node(int nodeIdentifier) {
        this.nodeIdentifier = nodeIdentifier;
    }

    private Node(int nodeIdentifier, List<Edge> adjacent) {
        this.nodeIdentifier = nodeIdentifier;
        this.adjacentEdges = new ArrayList<>(adjacent);
    }

    /**
     * @return The unique identifier of the node.
     */
    public int getNodeIdentifier() {
        return nodeIdentifier;
    }

    /**
     * @return The adjacent edges to the node.
     */
    public List<Edge> getAdjacentEdges() {
        return adjacentEdges;
    }

    /**
     * @return Logically equal copy of this Node
     */
    @Override
    public Node clone() {
        return new Node(this.nodeIdentifier, new ArrayList<Edge>(this.adjacentEdges));
    }

    /**
     * Logical equality checking for Node objects, falls back to superclass for other object types
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if(obj.getClass().equals(Node.class)) {
            if(this.nodeIdentifier == ((Node)obj).getNodeIdentifier()) {
                if(this.adjacentEdges.equals(((Node) obj).getAdjacentEdges()));
                    return true;
            }
        }
        return super.equals(obj);
    }
}
