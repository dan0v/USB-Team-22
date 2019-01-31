package uk.ac.newcastle.team22.usb.firebase;

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
    FLOORS;

    /** Returns the identifier of the collection. */
    public String getCollectionIdentifier() {
        switch (this) {
            case FLOORS: return "floors";
            default: return null;
        }
    }

    /** Returns the class of each document in the collection. */
    public Class<? extends FirestoreConstructable> getDocumentType() {
        switch (this) {
            case FLOORS: return Floor.class;
            default: return null;
        }
    }
}