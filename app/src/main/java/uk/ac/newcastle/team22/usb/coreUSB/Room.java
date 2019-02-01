package uk.ac.newcastle.team22.usb.coreUSB;

import android.annotation.SuppressLint;

import java.util.Map;

import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;

/**
 * A class which represents a room in Urban Sciences Building.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class Room implements FirestoreConstructable<Room> {

    /** The floor on which the room is situated. */
    private Floor floor;

    /** The room number on a given floor. */
    private int number;

    /** The identifier of the staff member who occupies this room */
    private String staffResidenceIdentifier;

    @Override
    public Room initFromFirebase(Map<String, Object> firestoreDictionary) {
        int number = ((Long) firestoreDictionary.get("number")).intValue();
        String staffResidenceIdentifier = (String) firestoreDictionary.get("staffResidenceIdentifier");

        this.number = number;
        this.staffResidenceIdentifier = staffResidenceIdentifier;
        return this;
    }

    /** Empty constructor. */
    public Room() {}

    /**
     * Helper method to set the floor which this room is situated.
     *
     * @param floor The floor which this room is situated.
     */
    public void attachFloor(Floor floor) {
        this.floor = floor;
    }

    /** Returns the floor on which the room is situated. */
    public Floor getFloor() {
        return floor;
    }


    // /** Returns the staff member who occupies this room. */
    /*
    public StaffMember getResidentStaff() {
        return USBManager.shared.staff.get(staffResidenceIdentifier);
    }
    */

    /**
     * Returns the formatted room number.
     * The formatted room number includes the floor number.
     */
    @SuppressLint("DefaultLocale")
    public String getNumber() {
        String floorNumber = String.valueOf(floor.getNumber());
        String roomNumber = String.format("%03d", number);
        return floorNumber + "." + roomNumber;
    }

    @Override
    public String toString() {
        return getNumber();
    }
}