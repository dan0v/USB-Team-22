package uk.ac.newcastle.team22.usb.navigation;

import java.util.ArrayList;
import java.util.List;

import uk.ac.newcastle.team22.usb.coreUSB.USBManager;

/**
 * @author Daniel Vincent
 * @version 1.0
 */
public class Node {
    private int nodeID;
    private int floorNumber;
    private List<Edge> edges = new ArrayList<Edge>();

    /**
     * Public constructor.
     * @param nodeID Identifier of Node in <pre>USBManager.sharedNodes</>.
     * @param floorNumber
     */
    public Node(int nodeID, int floorNumber) {
        this.nodeID = nodeID;
        this.floorNumber = floorNumber;
        //add this Node to the shared map of Nodes for access during Navigation
        USBManager.sharedNodes.put(nodeID, this);
    }

    /**
     * @return Unique ID of this Node.
     */
    public int getNodeID() {
        return this.nodeID;
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
            if(this.nodeID == that.getNodeID() && this.floorNumber == that.getFloorNumber() && this.edges.equals(that.getEdges())) {
                    return true;
            }
        }
        return super.equals(obj);
    }
}
