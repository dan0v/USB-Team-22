package uk.ac.newcastle.team22.usb.coreUSB;

import java.util.Map;

import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;
/**
 * A class which represents an item on the cafe menu.
 *
 * @author Patrick Lindley
 * @version 1.0
 */

public class CafeMenuItem implements FirestoreConstructable<CafeMenuItem>{
    private int price;
    private String itemName;

    /** Empty constructor. */
    public CafeMenuItem() {}

    @Override
    public CafeMenuItem initFromFirebase(Map<String, Object> firestoreDictionary, String documentIdentifier) {
        String itemName = (String) firestoreDictionary.get("itemName");
        int price = (int) firestoreDictionary.get("price");

        this.itemName = itemName;
        this.price = price;
        return this;
    }

    public CafeMenuItem(int price, String itemName) {
        this.price = price;
        this.itemName = itemName;
    }

    public int getPrice() {
        return price;
    }
    public String getItemName() {
        return itemName;
    }

    @Override
    public String toString() {
        return getItemName() + " " + getPrice();
    }
}
