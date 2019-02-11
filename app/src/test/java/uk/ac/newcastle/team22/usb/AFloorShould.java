package uk.ac.newcastle.team22.usb;

import org.junit.Test;

import uk.ac.newcastle.team22.usb.coreUSB.Floor;
import uk.ac.newcastle.team22.usb.coreUSB.Room;

import static org.junit.Assert.assertEquals;

/**
 * A test class for {@link Floor}.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class AFloorShould {

    @Test
    public void holdAValidFloorNumber() {
        Floor firstFloor = new Floor(1);
        assertEquals(firstFloor.getNumber(), "1");
    }

    @Test
    public void haveRooms() {
        Floor firstFloor = new Floor(1);
        new Room(firstFloor, 32);

        assertEquals(firstFloor.getRooms().size(), 1);
    }
}