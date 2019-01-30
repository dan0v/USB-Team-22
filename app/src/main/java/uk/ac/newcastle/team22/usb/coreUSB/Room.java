package uk.ac.newcastle.team22.usb.coreUSB;

import android.annotation.SuppressLint;

/**
 * A class which represents a room in USB.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class Room {

    /** The floor on which the room is situated. */
    private Floor floor;

    /** The room number on a given floor. */
    private int number;

    public Room(int number, Floor floor) {
        this.number = number;
        this.floor = floor;
        this.floor.attachRoom(this);
    }

    /** Returns the floor on which the room is situated. */
    public Floor getFloor() {
        return floor;
    }

    /**
     * Returns the formatted room number.
     * The formatted room number includes the floor number.
     */
    public String getNumber() {
        String floorNumber = String.valueOf(floor.getNumber());
        String roomNumber = String.format("%03d", number);
        return floorNumber + "." + roomNumber;
    }
}