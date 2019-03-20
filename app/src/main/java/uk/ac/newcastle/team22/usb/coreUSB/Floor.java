package uk.ac.newcastle.team22.usb.coreUSB;

import android.graphics.Color;

import java.util.*;

import uk.ac.newcastle.team22.usb.coreApp.ColorUtility;
import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;

/**
 * A class which represents a floor in Urban Sciences Building.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class Floor implements FirestoreConstructable<Floor> {

    /** The floor number. */
    private int number;

    /**
     * The rooms which are situated on the floor.
     * See {@link Room} for more information.
     */
    private Map<Integer, Room> rooms;

    /** The color which represents the floor. */
    private Color color;

    public Floor(int number) {
        this.number = number;
    }

    /** Empty constructor. */
    public Floor() {}

    @Override
    public Floor initFromFirebase(Map<String, Object> firestoreDictionary, String documentIdentifier) {
        int number = Integer.parseInt(documentIdentifier);
        Map<String, Long> color = (Map<String, Long>) firestoreDictionary.get("color");

        this.number = number;
        this.color = ColorUtility.valueOf(color);
        this.rooms = new HashMap();
        return this;
    }

    /**
     * Helper method to place a room onto a floor.
     *
     * @param room The room which is to be situated on the floor.
     */
    public void attachRoom(Room room) {
        this.rooms.put(room.getNumber(), room);
    }

    /**
     * @return The floor number.
     */
    public int getNumber() {
        return number;
    }

    /**
     * @return The rooms which are situated on the floor.
     */
    public Map<Integer, Room> getRooms() {
        return rooms;
    }

    /**
     * @return The color which represents the floor.
     */
    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Floor (number: " + getNumber() + ")";
    }
}
