package uk.ac.newcastle.team22.usb.coreUSB;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;

/**
 * An enum which defines the type of {@link Resource}.
 *
 * @author Daniel Vincent
 * @author Alexander MacLeod
 * @version 1.0
 */
public enum ResourceType {
    COMPUTER(1), PRINTER(2), WHITEBOARD(3), PROJECTOR(4), WORKSPACE(5);

    /** The identifier of the resource type. */
    private int identifier;

    /**
     * Constructor for {@link ResourceType} using its raw value.
     * @param identifier The integer representation or raw value of the resource type.
     */
    ResourceType(int identifier) {
        this.identifier = identifier;
    }

    /**
     * Returns the {@link ResourceType} for a given identifier.
     *
     * @param rawIdentifier The identifier of the resource type.
     * @return The {@link ResourceType}.
     * @throws FirestoreConstructable.InitialisationFailed
     */
    public static ResourceType valueFor(String rawIdentifier) throws FirestoreConstructable.InitialisationFailed {
        int identifier = Integer.parseInt(rawIdentifier);
        for (ResourceType type : ResourceType.values()) {
            if (type.identifier == identifier) {
                return type;
            }
        }
        throw new FirestoreConstructable.InitialisationFailed("Unknown resource type identifier '" + identifier + "'");
    }

    /**
     * @return Localised string representation of the resource type.
     */
    public @StringRes
    int getLocalisedResourceType() {
        switch (this) {
            case COMPUTER:      return R.string.computer;
            case PRINTER:       return R.string.printer;
            case WHITEBOARD:    return R.string.whiteboard;
            case PROJECTOR:     return R.string.projector;
            case WORKSPACE:     return R.string.workspace;
            default:            return 0;
        }
    }

    /**
     * @return Image representation of the resource type.
     */
    @DrawableRes
    public int getImageRepresentation() {
        switch (this) {
            case COMPUTER:
                return R.drawable.computer;
            case PRINTER:
                return R.drawable.printer;
            case WHITEBOARD:
                return R.drawable.whiteboard;
            case PROJECTOR:
                return R.drawable.projector;
            case WORKSPACE:
                return R.drawable.workspace;
            default:
                return 0;
        }
    }

    /**
     * @return The identifier of the resource.
     */
    public int getIdentifier() {
        return identifier;
    }
}
