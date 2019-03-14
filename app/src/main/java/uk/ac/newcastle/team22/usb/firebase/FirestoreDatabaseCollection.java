package uk.ac.newcastle.team22.usb.firebase;

import org.jetbrains.annotations.NotNull;
import uk.ac.newcastle.team22.usb.coreUSB.*;
import uk.ac.newcastle.team22.usb.navigation.Node;

/**
 * A Firestore Database collection.
 * <p>
 * See <a href="https://firebase.google.com/docs/firestore/data-model">Firestore Data Model</a> for more information on collections.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public enum FirestoreDatabaseCollection {
    FLOORS, ROOMS, STAFF, CAFE_MENU, NAVIGATION_NODES, OPENING_HOURS;

    /** Returns the identifier of the collection. */
    @NotNull
    public String getCollectionIdentifier() {
        switch (this) {
            case FLOORS: return "floors";
            case ROOMS: return "rooms";
            case STAFF: return "staffMembers";
            case CAFE_MENU: return "cafeMenu";
            case NAVIGATION_NODES: return "nodes";
            case OPENING_HOURS: return  "openingTimes";
            default: return null;
        }
    }

    /** Returns the class of each document in the collection. */
    public Class<? extends FirestoreConstructable> getDocumentType() {
        switch (this) {
            case FLOORS: return Floor.class;
            case ROOMS: return Room.class;
            case STAFF: return StaffMember.class;
            case CAFE_MENU: return CafeMenuItem.class;
            case NAVIGATION_NODES: return Node.class;
            case OPENING_HOURS: return OpeningHours.class;
            default: return null;
        }
    }
}