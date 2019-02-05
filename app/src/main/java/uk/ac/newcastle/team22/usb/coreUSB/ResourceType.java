package uk.ac.newcastle.team22.usb.coreUSB;

/**
 * An enum which defines the type of {@link Resource}.
 *
 * @author Daniel Vincent
 * @author Alexander MacLeod
 * @version 1.0
 */
public enum ResourceType {
    COMPUTER(1), PRINTER(2), WHITEBOARD(3), PROJECTOR(4);

    /** The identifier of the resource type. */
    private int identifier;

    /**
     * Constructor for {@link ResourceType} using raw value.
     * @param identifier The integer representation or raw value of the type.
     */
    ResourceType(int identifier) {
        this.identifier = identifier;
    }

    /**
     * Returns the {@link ResourceType} for a given identifier.
     * @param rawIdentifier The identifier of the resource.
     * @return The {@link ResourceType}.
     */
    public static ResourceType valueFor(String rawIdentifier) {
        int identifier = Integer.parseInt(rawIdentifier);
        for (ResourceType type : ResourceType.values()) {
            if (type.identifier == identifier) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown resource type with identifier '" + identifier +"'");
    }
}
