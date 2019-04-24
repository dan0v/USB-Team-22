package uk.ac.newcastle.team22.usb.navigation;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.ac.newcastle.team22.usb.coreUSB.USBManager;
import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;

/**
 * A class that represents an edge between two {@link Node} in the Urban Sciences Building.
 * See {@link Node} for more information on nodes.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public class Edge {

    /** The origin node. */
    private final Node origin;

    /** The destination node identifier. */
    private final int destinationIdentifier;

    /** The weight of the edge. */
    public final double weight;

    /** The directions from the edge. */
    public final List<Integer> directions;

    /** The distances from the edge. */
    public final List<Double> distances;

    /** Boolean value whether the edge requires card authorization. */
    public final boolean cardLocked;

    /** Boolean value whether the edge is accessible. */
    public final boolean accessible;

    /**
     * Construct an edge between nodes.
     *
     * @param origin Origin node of this edge.
     * @param destinationIdentifier Identifier of destination node of this edge.
     * @param weight Total weight of the path this edge represents.
     * @param directions List of direction - Copy is made.
     * @param distances List of distances for each direction - Copy is made.
     * @param cardLocked boolean whether card access is required.
     * @param accessible boolean whether this edge meets accessibility needs.
     */
    public Edge(Node origin, int destinationIdentifier, double weight, List<Integer> directions, List<Double> distances, boolean cardLocked, boolean accessible) {
        this.origin = origin;
        this.destinationIdentifier = destinationIdentifier;
        this.weight = weight;
        this.directions = new ArrayList<>(directions);
        this.distances = new ArrayList<>(distances);
        this.cardLocked = cardLocked;
        this.accessible = accessible;
    }

    /**
     * Initialise an edge from Firebase.
     *
     * @param origin Origin node of this edge.
     * @param firestoreDictionary Map representation of this edge in Firebase.
     */
    public Edge(Node origin, Map<String, Object> firestoreDictionary) throws FirestoreConstructable.InitialisationFailed {
        this.origin = origin;
        this.destinationIdentifier = ((Number) firestoreDictionary.get("destination")).intValue();
        this.weight = ((Number) firestoreDictionary.get("weight")).doubleValue();
        this.cardLocked = (boolean) firestoreDictionary.get("cardLocked");
        this.accessible = (boolean) firestoreDictionary.get("accessible");

        // Initialise edge directions.
        this.directions = new ArrayList<>();
        List<Number> firestoreDirections = (ArrayList<Number>) firestoreDictionary.get("directions");
        if (firestoreDirections != null) {
            for (Number firestoreDirection : firestoreDirections) {
                this.directions.add(firestoreDirection.intValue());
            }
        } else {
            throw new FirestoreConstructable.InitialisationFailed("Edge could not be initialised - missing directions");
        }

        // Initialise edge distances.
        this.distances = new ArrayList<>();
        List<Number> firestoreDistances = (ArrayList<Number>) firestoreDictionary.get("distances");
        if (firestoreDirections != null) {
            for (Number firestoreDistance : firestoreDistances) {
                this.distances.add(firestoreDistance.doubleValue());
            }
        } else {
            throw new FirestoreConstructable.InitialisationFailed("Edge could not be initialised - missing distances");
        }
    }

    /**
     * @return Origin node of this edge.
     */
    public Node getOrigin() {
        return this.origin;
    }

    /**
     * @return Node with {@code nodeIdentifier} of this edge from {@code USB.navigationNodes}.
     */
    public Node getDestination() {
        if (USBManager.shared.getBuilding().getNavigationNodes().get(this.destinationIdentifier) == null) {
            String msg = String.format("A destination node of the edge between node: %s and: %s is not in the node map", this.origin.getNodeIdentifier(), this.destinationIdentifier);
            Log.e("Navigator", msg);
            return this.origin;
        }
        return USBManager.shared.getBuilding().getNavigationNodes().get(this.destinationIdentifier);
    }

    /**
     * Logical equality checking for edges, falls back to superclass for unknown object types.
     *
     * @param obj Object to compare to {@code this}.
     * @return @{code true} if edges are logically equivalent, or objects have the same hash, otherwise {@code false}.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(Edge.class)) {
            Edge that = (Edge) obj;
            if (this.getOrigin().equals(that.getOrigin()) && this.getDestination().equals(that.getDestination()) && this.weight == that.weight && this.directions.equals(that.directions) && this.distances.equals(that.distances) && this.cardLocked == that.cardLocked && this.accessible == that.accessible) {
                return true;
            }
        }
        return super.equals(obj);
    }

    /**
     * @return String representation of an edge.
     */
    @Override
    public String toString() {
        String stringRep = String.format("Origin: %s Destination: %s Directions: %s Distances: %s Accessible?: %s CardLocked?: %s\n", this.getOrigin().getNodeIdentifier(), this.getDestination().getNodeIdentifier(), this.directions, this.distances, this.accessible, this.cardLocked);
        return stringRep;
    }
}
