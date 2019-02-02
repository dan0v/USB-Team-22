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

    /**
     * Constructs the object from a Firestore document dictionary.
     *
     * @param firestoreDictionary The Firestore document dictionary.
     * @param documentIdentifier The identifier of the Firestore document.
     * @return The initialised object.
     */
    T initFromFirebase(Map<String, Object> firestoreDictionary, String documentIdentifier);
}