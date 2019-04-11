package uk.ac.newcastle.team22.usb;

import android.graphics.Color;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import uk.ac.newcastle.team22.usb.coreUSB.Floor;
import uk.ac.newcastle.team22.usb.coreUSB.Resource;
import uk.ac.newcastle.team22.usb.coreUSB.ResourceType;
import uk.ac.newcastle.team22.usb.coreUSB.Room;
import uk.ac.newcastle.team22.usb.coreUSB.StaffMember;
import uk.ac.newcastle.team22.usb.navigation.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

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
    /** The room's resources. */
    List<Resource> resources = new ArrayList<>();
//2.046
    @Before
    public void setUp() {
        Color yellow = new Color();
        floor = new Floor(2, yellow);
        room = new Room(floor, "46" , resources, "Alan Tully", 109 , "Lecture Theatre");
        room.attachFloor(floor);
    }

    /** Assert rooms can have a floor */
    @Test
    public void beSituatedOnAFloor() {
        assertEquals(room.getFloor(), floor);
    }

    /** Assert room can hold a room number */
    @Test
    public void holdAValidRoomNumber() {
        assertEquals("46", room.getNumber());
    }

    /** Assert a room can hold an alternate room name */
    @Test
    public void beAbleToHoldAnAlternateName() { assertEquals("Lecture Theatre", room.getAlternateName()); }

    /** Assert a room can hold resources of multiple types */
    @Test
    public void beAbleToHoldResources() {
        resources.add(new Resource(ResourceType.COMPUTER, 57));
        resources.add(new Resource(ResourceType.PRINTER, 1));
        resources.add(new Resource(ResourceType.WHITEBOARD, 2));
        resources.add(new Resource(ResourceType.PROJECTOR, 1));
        resources.add(new Resource(ResourceType.WORK_SPACE, 57));

        /** Assert resources have been added to collection */
        assertNotNull(resources);
        /** Assert 2nd resource is of type PRINTER */
        assertEquals(room.getResources().get(1).getType(), ResourceType.PRINTER);
    }

    /** Assert the room can hold a staff member as a string field */
    @Test
    public void haveAStaffResidentIdentifier() {
        assertEquals(room.getStaffResidenceIdentifier(), "Alan Tully");
    }

    /*
   @Test
   // Returns null from USBManager class 'getBuilding' when debugged
   public void haveANearestNode() {
   assertNotNull(room.getNavigationNode());
    }
    */

    /** Assert room name can be correctly formatted and returned when alternate room name != NULL */
    @Test
    public void formatNameCorrectlyWithAlternateRoomName() {
        String name = "Lecture Theatre, 2.046";
        assertEquals(name, room.getFormattedName());
    }
    /** Assert room name can be correctly formatted and returned when alternate room name = NULL */
    @Test
    public void formatNameCorrectlyWithoutAlternateRoomName() {
        Room roomTwo = new Room(floor, "46" , resources, "Alan Tully", 109 , null);
        String name = "Room 2.046";
        assertEquals(name, roomTwo.getFormattedName());
    }

    /** Assert room number can be formatted correctly given floor and initial room number */
    @Test
    public void formatRoomNumberCorrectly() {
        assertEquals("2.046", room.getFormattedNumber());
    }

    /** Assert a StaffMember object can be instantiated given the staff resident identifier
    @Test
    public void haveAResidentStaffMember() {
       StaffMember alan = new StaffMember("Dr", "Alan", "Tully", "01912088223", "alan.tully@newcastle.ac.uk");
       assertEquals(alan, room.getResidentStaff());
    }
     */

    /** Assert that, once updated, the new computer resource is different from the old for a given room
     *  (and this information can be retrieved)
     */
    @Test
    public void beAbleToUpdateComputerAvailability() {
        Resource oldComputers = room.getComputers();
        room.updateComputerAvailability(new Resource(ResourceType.COMPUTER, 3));
        assertNotEquals(room.getComputers(), oldComputers);
    }

    /** Assert that a room can be correctly returned as a string representation */
    @Test
    public void returnedAsAStringRepresentation() {
        assertEquals("2.046", room.toString());
    }

}