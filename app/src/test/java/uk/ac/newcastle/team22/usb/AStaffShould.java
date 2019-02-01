package uk.ac.newcastle.team22.usb;

import org.junit.Test;
import uk.ac.newcastle.team22.usb.coreUSB.*;
import static org.junit.Assert.*;

/**
 * A test class for {@link Staff}.
 *
 * @author Patrick Lindley
 * @version 1.0
 */

public class AStaffShould {

    @Test
    public void haveAFirstName() {
        Floor testFloor = new Floor();
        Room testRoom = new Room(1, testFloor);
        Staff newStaff = new Staff("Patrick", "Lindley",
                "0111111111", "test@ncl.ac.uk", testRoom);
        assertEquals("Patrick", newStaff.getFirstName());
    }

    @Test
    public void haveALastName() {
        Floor testFloor = new Floor();
        Room testRoom = new Room(1, testFloor);
        Staff newStaff = new Staff("Patrick", "Lindley",
                "0111111111", "test@ncl.ac.uk", testRoom);
        assertEquals("Lindley", newStaff.getLastName());
    }

    @Test
    public void haveAPhoneNumber() {
        Floor testFloor = new Floor();
        Room testRoom = new Room(1, testFloor);
        Staff newStaff = new Staff("Patrick", "Lindley",
                "0111111111", "test@ncl.ac.uk", testRoom);
        assertEquals("0111111111", newStaff.getPhoneNumber());
    }

    @Test
    public void haveAnEmailAddress() {
        Floor testFloor = new Floor();
        Room testRoom = new Room(1, testFloor);
        Staff newStaff = new Staff("Patrick", "Lindley",
                "0111111111", "test@ncl.ac.uk", testRoom);
        assertEquals("test@ncl.ac.uk", newStaff.getEmailAddress());
    }

    @Test
    public void haveAnAssignedRoom() {
        Floor testFloor = new Floor();
        Room testRoom = new Room(1, testFloor);
        Staff newStaff = new Staff("Patrick", "Lindley",
                "0111111111", "test@ncl.ac.uk", testRoom);
        assertEquals("0111111111", newStaff.getRoomNo());
    }
}
