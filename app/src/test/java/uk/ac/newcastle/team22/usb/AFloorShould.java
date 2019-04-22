package uk.ac.newcastle.team22.usb;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import uk.ac.newcastle.team22.usb.coreUSB.Floor;
import uk.ac.newcastle.team22.usb.coreUSB.Resource;
import uk.ac.newcastle.team22.usb.coreUSB.Room;
import uk.ac.newcastle.team22.usb.coreUSB.StaffMember;
import uk.ac.newcastle.team22.usb.coreUSB.USB;
import uk.ac.newcastle.team22.usb.coreUSB.USBManager;
import uk.ac.newcastle.team22.usb.coreUSB.USBUpdate;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * A test class for {@link Floor}.
 *
 * @author Alexander MacLeod
 * @author Daniel Walker
 * @version 1.0
 */
public class AFloorShould {

    /** The floor to be attached to the test room. */
    Floor floor;

    /** The room to be tested. */
    Room room;

    /** The staff member to be tested. */
    StaffMember staffMember;

    /** The room's resources. */
    List<Resource> resources = new ArrayList<>();

    @Before
    public void setUp() {
        // Create floor with one room.
        List<Floor> floors = new ArrayList<>();
        room = new Room(floor, "46" , resources, "Alan Tully", 109 , "Lecture Theatre");
        floor = new Floor(3, 0);
        room.attachFloor(floor);
        floor.attachRoom(room);
        floors.add(floor);

        // Create staff member.
        List<StaffMember> members = new ArrayList<>();
        staffMember = new StaffMember("Alan Tully", "Dr", "Alan", "Tully", "01912088223", "alan.tully@newcastle.ac.uk");
        members.add(staffMember);

        // Create a custom building update.
        USBUpdate update = new USBUpdate();
        update.setFloors(floors);
        update.setStaffMembers(members);

        // Set the custom building.
        USB building = new USB(update);
        USBManager.shared.setBuilding(building);
    }

    /** Assert a floor can have a colour. */
    @Test
    public void haveAColour() {
        assertNotNull(floor.getColor());
    }

    /** Assert a floor can be represented as a string. */
    @Test
    public void haveAStringRepresentation() {
        assertEquals("Floor (number: 3)", floor.toString());
    }

    /** Assert the floor number is in the range 0-6 inclusive. */
    @Test
    public void holdAValidFloorNumber() {
        int num = floor.getNumber();
        assertTrue(0 <= num && num <= 6);
    }
}