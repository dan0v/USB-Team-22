package uk.ac.newcastle.team22.usb.coreUSB;

import java.util.Map;

import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;

public abstract class USBConfiguration implements FirestoreConstructable<USBConfiguration> {

    private int version = 0;

    public int getVersion() {
        return version;
    }

    public USBConfiguration initFromFirebase(Map<String, Object> firestoreDictionary,String documentIdentifier) {

        int version = ((Number) firestoreDictionary.get("version")).intValue();
        this.version = version;
        return this;

    }

}
