package uk.ac.newcastle.team22.usb.coreUSB;

import android.util.Log;

import java.util.Map;

import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;

public class USBConfiguration implements FirestoreConstructable<USBConfiguration> {

    private int version = 0;

    public int getVersion() {
        return version;
    }

    public USBConfiguration() {}

    public USBConfiguration initFromFirebase(Map<String, Object> firestoreDictionary,String documentIdentifier) {
        Log.i("", firestoreDictionary.toString());
        int version = ((Number) firestoreDictionary.get("version")).intValue();
        this.version = version;
        return this;

    }

}
