package uk.ac.newcastle.team22.usb.coreUSB;

import android.util.Log;

import java.util.*;
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
    private List<Room> rooms = new ArrayList<>();

    @Override
    public Floor initFromFirebase(Map<String, Object> firestoreDictionary, String documentIdentifier) {
        int number = Integer.parseInt(documentIdentifier);
        this.number = number;
        return this;
    }

    /** Empty constructor. */
    public Floor() {}

    /**
     * Helper method to place a room onto a floor.
     *
     * @param room The room which is to be situated on the floor.
     */
    public void attachRoom(Room room) {
        this.rooms.add(room);
    }

    /** Returns the rooms which are situated on the floor. */
    public List<Room> getRooms() {
        return rooms;
    }

    /** Returns the floor number. */
    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Floor Number: " + getNumber();
    }
}
