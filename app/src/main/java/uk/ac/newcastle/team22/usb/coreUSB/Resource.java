package uk.ac.newcastle.team22.usb.coreUSB;

import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;

/**
 * A class which represents a resource in a {@link Room}.
 *
 * @author Daniel Vincent
 * @author Alexander MacLeod
 * @version 1.0
 */
public class Resource {

    /** The type of resource. */
    private ResourceType type;

    /** The number of this resource which is available. */
    private int available;

    /**
     * Constructor for {@link Resource}.
     * @param resourceTypeIdentifier The identifier of the resource type.
     * @param available The number of this resource which is available.
     * @throws FirestoreConstructable.InitialisationFailed
     */
    public Resource(String resourceTypeIdentifier, int available) throws FirestoreConstructable.InitialisationFailed {
        ResourceType type = ResourceType.valueFor(resourceTypeIdentifier);

        this.type = type;
        this.available = available;
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
}
