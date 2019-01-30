package uk.ac.newcastle.team22.usb.coreUSB;

import java.util.ArrayList;
import java.util.List;

/**
 * A class which represents a floor in USB.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class Floor {

    /** The floor number */
    private int number;

    /**
     * The rooms which are situated on the floor.
     * See {@link Room} for more information.
     */
    private List<Room> rooms = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param number The floor number.
     *
     */
    public Floor(int number) {
        this.number = number;
    }

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
}
