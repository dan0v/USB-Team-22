package uk.ac.newcastle.team22.usb;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import uk.ac.newcastle.team22.usb.coreUSB.CafeMenuItem;
import uk.ac.newcastle.team22.usb.coreUSB.Floor;
import uk.ac.newcastle.team22.usb.coreUSB.StaffMember;
import uk.ac.newcastle.team22.usb.coreUSB.USB;
import uk.ac.newcastle.team22.usb.coreUSB.USBManager;
import uk.ac.newcastle.team22.usb.coreUSB.USBUpdate;

import static junit.framework.TestCase.assertNotNull;

/**
 * A test class for {@link uk.ac.newcastle.team22.usb.coreUSB.USB}.
 *
 * @author Patrick Lindley
 * @version 1.0
 */
public class AUSBShould {

    @Before
    public void setUp() {
        //Creates a USB with one floor, cafe and staff member
        List<Floor> floors = new ArrayList<>();
        Floor floor = new Floor(3, 0);
        floors.add(floor);

        List<StaffMember> staffs = new ArrayList<>();
        StaffMember staff = new StaffMember("Alan Tully", "Dr", "Alan", "Tully", "01912088223", "alan.tully@newcastle.ac.uk");
        staffs.add(staff);

        List<CafeMenuItem> items = new ArrayList<>();
        CafeMenuItem newItem = new CafeMenuItem();
        items.add(newItem);

        USBUpdate update = new USBUpdate();
        update.setFloors(floors);
        update.setStaffMembers(staffs);
        update.setCafeMenuItems(items);

        USB building = new USB(update);
        USBManager.shared.setBuilding(building);
    }

    /** Assert that a building should have floors. */
    @Test
    public void haveFloors(){
        assertNotNull(USBManager.shared.getBuilding().getFloors());
    }

    /** Assert that a building should staff members. */
    @Test
    public void haveStaffMembers() {
        assertNotNull(USBManager.shared.getBuilding().getStaffMembers());
    }

    /** Assert that a building should have a café. */
    @Test
    public void haveCafe() {
        assertNotNull(USBManager.shared.getBuilding().getCafe());
    }
}
