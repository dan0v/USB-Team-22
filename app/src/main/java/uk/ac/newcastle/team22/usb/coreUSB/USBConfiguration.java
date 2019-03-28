package uk.ac.newcastle.team22.usb.coreUSB;

import java.util.Map;

import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;

/**
 * A class which manages the configuration of the Urban Sciences Building.
 *
 * @author Alex Beeching
 * @author Alexander MacLeod
 * @version 1.0
 */
public class USBConfiguration implements FirestoreConstructable<USBConfiguration> {

    /** The version of the Urban Sciences Building. */
    private int version;

    public USBConfiguration initFromFirebase(Map<String, Object> firestoreDictionary,String documentIdentifier) {
        int version = ((Number) firestoreDictionary.get("version")).intValue();
        this.version = version;
        return this;
    }

    public USBConfiguration() {}

    /**
     * @return The version of the Urban Sciences Building.
     */
    public int getVersion() {
        return version;
    }
}
