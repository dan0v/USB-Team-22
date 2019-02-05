package uk.ac.newcastle.team22.usb.firebase;

import java.util.Map;

/**
 * An interface which declares an object as constructable from a Firestore document dictionary.
 * See {@link FirebaseManager} for more information on retrieving Firestore document dictionaries.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public interface FirestoreConstructable<T> {

    /** The exception to throw when the initialisation of a {@link FirestoreConstructable} object fails. */
    class InitialisationFailed extends Exception {
        public InitialisationFailed(String message) {
            super(message);
        }
    }

    /**
     * Constructs the object from a Firestore document dictionary.
     *
     * @param firestoreDictionary The Firestore document dictionary.
     * @param documentIdentifier The identifier of the Firestore document.
     * @return The initialised object.
     * @throws InitialisationFailed
     */
    T initFromFirebase(Map<String, Object> firestoreDictionary, String documentIdentifier) throws InitialisationFailed;
}