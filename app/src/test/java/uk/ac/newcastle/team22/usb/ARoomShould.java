package uk.ac.newcastle.team22.usb;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import uk.ac.newcastle.team22.usb.coreUSB.Floor;
import uk.ac.newcastle.team22.usb.coreUSB.Resource;
import uk.ac.newcastle.team22.usb.coreUSB.ResourceType;
import uk.ac.newcastle.team22.usb.coreUSB.Room;
import uk.ac.newcastle.team22.usb.coreUSB.StaffMember;
import uk.ac.newcastle.team22.usb.coreUSB.USB;
import uk.ac.newcastle.team22.usb.coreUSB.USBManager;
import uk.ac.newcastle.team22.usb.coreUSB.USBUpdate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * A test class for {@link Room}.
 *
 * @author Alexander MacLeod
 * @author Daniel Walker
 * @version 1.0
 */
public class ARoomShould {

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
        floor = new Floor(2, 0);
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

    /** Assert rooms can have a floor. */
    @Test
    public void beSituatedOnAFloor() {
        assertEquals(room.getFloor(), floor);
    }

    /** Assert room can hold a room number. */
    @Test
    public void holdAValidRoomNumber() {
        assertEquals("46", room.getNumber());
    }

    /** Assert a room can hold an alternate room name. */
    @Test
    public void beAbleToHoldAnAlternateName() { assertEquals("Lecture Theatre", room.getAlternateName()); }

    /** Assert a room can hold resources of multiple types. */
    @Test
    public void beAbleToHoldResources() {
        resources.add(new Resource(ResourceType.COMPUTER, 57));
        resources.add(new Resource(ResourceType.PRINTER, 1));
        resources.add(new Resource(ResourceType.WHITEBOARD, 2));
        resources.add(new Resource(ResourceType.PROJECTOR, 1));
        resources.add(new Resource(ResourceType.WORKSPACE, 57));

        // Assert 2nd resource is of type PRINTER.
        assertEquals(room.getResources().get(1).getType(), ResourceType.PRINTER);

        // Assert 2nd resource has one PRINTER available.
        assertEquals(room.getResources().get(1).getAvailable(), 1);
    }

    /** Assert the room can hold a staff member. */
    @Test
    public void haveAStaffResidentIdentifier() {
        assertEquals(room.getStaffResidenceIdentifier(), "Alan Tully");
    }

    /** Assert room number can be formatted correctly given floor and initial room number. */
    @Test
    public void formatRoomNumberCorrectly() {
        assertEquals("2.046", room.getFormattedNumber());
    }

    /** Assert a StaffMember object can be instantiated given the staff resident identifier. */
    @Test
    public void haveAResidentStaffMember() {
        assertEquals(staffMember, room.getResidentStaff());
    }

    /** Assert that, once updated, the new computer resource is different from the old for a given room. */
    @Test
    public void beAbleToUpdateComputerAvailability() {
        Resource oldComputers = room.getComputers();
        room.updateComputerAvailability(new Resource(ResourceType.COMPUTER, 3));
        assertNotEquals(room.getComputers(), oldComputers);
    }

    /** Assert that a room can be correctly returned as a string representation. */
    @Test
    public void returnedAsAStringRepresentation() {
        assertEquals("2.046", room.toString());
    }
}