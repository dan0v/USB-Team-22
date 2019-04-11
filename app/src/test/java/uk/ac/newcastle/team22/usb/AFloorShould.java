package uk.ac.newcastle.team22.usb;

import android.graphics.Color;


import org.junit.*;

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import uk.ac.newcastle.team22.usb.coreUSB.Floor;
import uk.ac.newcastle.team22.usb.coreUSB.Resource;
import uk.ac.newcastle.team22.usb.coreUSB.Room;

/**
 * A test class for {@link Floor}.
 *
 * @author Alexander MacLeod
 * @author Daniel Walker
 * @version 1.0
 */
public class AFloorShould {

    Color yellow;
    Floor floor;
    Room room;
    Room roomTwo;
    List<Resource> resources;
    @Before
    public void setUp() {
        yellow = new Color();
        floor = new Floor(3, yellow);
        room = new Room(floor, "46" , resources, "Alan Tully", 109 , "Lecture Theatre");

    }
/**
    @Test
    public void beAbleToAttachRooms() {
        floor.attachRoom(room);
        assertNotNull(floor.getRooms());
    }
*/
    @Test
    public void haveAColour() {
        assertNotNull(floor.getColor());
    }

    @Test
    public void haveAStringRepresentation() {
        assertEquals("Floor (number: 3)", floor.toString());
    }

    @Test
    public void holdAValidFloorNumber() {
        int num = floor.getNumber();
        assertTrue(0 <= num && num <= 6);
    }

}