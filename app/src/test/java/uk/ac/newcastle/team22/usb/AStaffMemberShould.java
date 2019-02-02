package uk.ac.newcastle.team22.usb;

import org.junit.Test;
import uk.ac.newcastle.team22.usb.coreUSB.*;
import static org.junit.Assert.*;

/**
 * A test class for {@link StaffMember}.
 *
 * @author Patrick Lindley
 * @version 1.0
 */

public class AStaffMemberShould {

    @Test
    public void haveAFirstName() {
        Floor testFloor = new Floor();
        StaffMember newStaffMember = new StaffMember("Mr", "Patrick", "Lindley",
                "0111111111", "test@ncl.ac.uk");
        assertEquals("Patrick", newStaffMember.getFirstName());
    }

    @Test
    public void haveALastName() {
        Floor testFloor = new Floor();
        StaffMember newStaffMember = new StaffMember("Mr", "Patrick", "Lindley",
                "0111111111", "test@ncl.ac.uk");
        assertEquals("Lindley", newStaffMember.getLastName());
    }

    @Test
    public void haveAPhoneNumber() {
        Floor testFloor = new Floor();
        StaffMember newStaffMember = new StaffMember("Mr", "Patrick", "Lindley",
                "0111111111", "test@ncl.ac.uk");
        assertEquals("0111111111", newStaffMember.getPhoneNumber());
    }

    @Test
    public void haveAnEmailAddress() {
        Floor testFloor = new Floor();
        StaffMember newStaffMember = new StaffMember("Mr", "Patrick", "Lindley",
                "0111111111", "test@ncl.ac.uk");
        assertEquals("test@ncl.ac.uk", newStaffMember.getEmailAddress());
    }
}
