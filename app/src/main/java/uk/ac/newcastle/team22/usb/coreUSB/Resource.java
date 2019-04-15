package uk.ac.newcastle.team22.usb.coreUSB;

import java.util.ArrayList;
import java.util.List;

import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;
import uk.ac.newcastle.team22.usb.search.ResultReason;
import uk.ac.newcastle.team22.usb.search.Searchable;

/**
 * A class which represents a resource in a {@link Room}.
 *
 * @author Daniel Vincent
 * @author Alexander MacLeod
 * @version 1.0
 */
public class Resource implements Searchable {

    /** The type of resource. */
    private ResourceType type;

    /** The number of this resource which is available. */
    private int available;

    /** The number of this resource which is available in this location. */
    private int total;

    /** The room in which this resource is situated. */
    private Room room;

    public Resource(ResourceType resourceType, int available) {
        this.type = resourceType;
        this.available = available;
        this.total = 0;
    }

    public Resource(ResourceType resourceType, int available, int total) {
        this.type = resourceType;
        this.available = available;
        this.total = total;
    }

    /**
     * Constructor for {@link Resource}.
     *
     * @param resourceTypeIdentifier The identifier of the resource type.
     * @param available The number of this resource which is available.
     * @param room The room in which this resource is situated.
     * @throws FirestoreConstructable.InitialisationFailed
     */
    public Resource(String resourceTypeIdentifier, int available, Room room) throws FirestoreConstructable.InitialisationFailed {
        ResourceType type = ResourceType.valueFor(resourceTypeIdentifier);

        this.type = type;
        this.available = available;
        this.room = room;
    }

    /**
     * @return The resource type.
     */
    public ResourceType getType() {
        return type;
    }

    /**
     * @return The number of this resource which is available.
     */
    public int getAvailable() {
        return available;
    }
  
    /**
     * @return The number of this resource which is available at this location.
     */
    public int getTotal() {
        return total;
    }

    /**
     * @return The room in which this resource is situated.
     */
    public Room getRoom() {
        return room;
    }

    @Override
    public List<ResultReason> getSearchableReasons() {
        List<ResultReason> reasons = new ArrayList();
        reasons.add(new ResultReason(type.name(), ResultReason.Reason.RESOURCE));
        return reasons;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
