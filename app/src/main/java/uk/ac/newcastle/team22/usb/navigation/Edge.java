package uk.ac.newcastle.team22.usb.navigation;

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
    private final Node origin;
    private final int destinationIdentifier;
    public final int weight;
    public final List<Direction> directions;
    public final List<Integer> distances;
    public final boolean cardLocked;
    public final boolean accessible;

    /**
     * Construct an Edge between nodes.
     * @param origin Origin Node of this Edge.
     * @param destinationIdentifier Identifier of destination Node of this Edge.
     * @param weight Total weight of the path this Edge represents.
     * @param directions List of Direction - Copy is made.
     * @param distances List of distances for each direction - Copy is made.
     * @param cardLocked boolean whether card access is required.
     * @param accessible boolean whether this edge meets accessibility needs.
     */
    public Edge(Node origin, int destinationIdentifier, int weight, List<Direction> directions, List<Integer> distances, boolean cardLocked, boolean accessible) {
        this.origin = origin;
        this.destinationIdentifier = destinationIdentifier;
        this.weight = weight;
        this.directions = new ArrayList<>(directions);
        this.distances = new ArrayList<>(distances);
        this.cardLocked = cardLocked;
        this.accessible = accessible;
    }

    /**
     * Initialise an Edge from Firebase.
     * @param origin Origin Node of this Edge.
     * @param firestoreDictionary Map representation of this Edge in Firebase.
     */
    public Edge(Node origin, Map<String, Object> firestoreDictionary) throws FirestoreConstructable.InitialisationFailed {
        this.origin = origin;
        this.destinationIdentifier = ((Long) firestoreDictionary.get("destination")).intValue();
        this.weight = ((Long) firestoreDictionary.get("weight")).intValue();
        this.cardLocked = (boolean) firestoreDictionary.get("cardLocked");
        this.accessible = (boolean) firestoreDictionary.get("accessible");

        // Initialise edge directions.
        this.directions = new ArrayList<>();
        List<String> firestoreDirections = (ArrayList<String>) firestoreDictionary.get("directions");
        if (firestoreDirections != null) {
            for (String firestoreDirection : firestoreDirections) {
                if (firestoreDirection != null) {
                    this.directions.add(Direction.parseDirection(firestoreDirection));
                }
            }
        } else {
            throw new FirestoreConstructable.InitialisationFailed("Edge could not be initialised - missing directions");
        }

        // Initialise edge distances.
        this.distances = new ArrayList<>();
        List<Long> firestoreDistances = (ArrayList<Long>) firestoreDictionary.get("distances");
        if (firestoreDirections != null) {
            for (Long firestoreDistance : firestoreDistances) {
                this.distances.add(firestoreDistance.intValue());
            }
        } else {
            throw new FirestoreConstructable.InitialisationFailed("Edge could not be initialised - missing distances");
        }
    }

    /**
     * @return Origin Node of this Edge.
     */
    public Node getOrigin() {
        return this.origin;
    }

    /**
     * @return Node with nodeIdentifier of this Edge from <pre>USBManager.sharedNodes</pre>.
     */
    public Node getDestination() {
        return USBManager.shared.getBuilding().getNavigationNodes().get(destinationIdentifier);
    }

    /**
     * Logical equality checking for Edges, falls back to superclass for unknown object types.
     * @param obj Object to compare to <pre>this</pre>.
     * @return True if Edges are logically equivalent, or Objects have the same hash, false otherwise.
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
}
