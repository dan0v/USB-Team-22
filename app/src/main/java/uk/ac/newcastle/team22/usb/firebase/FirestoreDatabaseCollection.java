package uk.ac.newcastle.team22.usb.firebase;

import org.jetbrains.annotations.NotNull;
import uk.ac.newcastle.team22.usb.coreUSB.*;

/**
 * A Firestore Database collection.
 * <p>
 * See <a href="https://firebase.google.com/docs/firestore/data-model">Firestore Data Model</a> for more information on collections.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public enum FirestoreDatabaseCollection {
    FLOORS, ROOMS;

    /** Returns the identifier of the collection. */
    @NotNull
    public String getCollectionIdentifier() {
        switch (this) {
            case FLOORS: return "floors";
            case ROOMS: return "rooms";
            default: return null;
        }
    }

    /** Returns the class of each document in the collection. */
    public Class<? extends FirestoreConstructable> getDocumentType() {
        switch (this) {
            case FLOORS: return Floor.class;
            case ROOMS: return Room.class;
            default: return null;
        }
    }
}