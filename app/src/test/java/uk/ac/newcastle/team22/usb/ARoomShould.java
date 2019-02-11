package uk.ac.newcastle.team22.usb;

import org.junit.Test;
import uk.ac.newcastle.team22.usb.coreUSB.*;
import static org.junit.Assert.*;

/**
 * A test class for {@link Room}.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class ARoomShould {

    @Test
    public void beSituatedOnAFloor() {
        Floor firstFloor = new Floor(1);
        new Room(firstFloor, 32);

        assertEquals(firstFloor.getRooms().size(), 1);
    }

    @Test
    public void holdAValidRoomNumber() {
        Floor firstFloor = new Floor(1);
        Room someRoom = new Room(firstFloor, 32);

        assertEquals(someRoom.getNumber(), "1.032");
    }
}