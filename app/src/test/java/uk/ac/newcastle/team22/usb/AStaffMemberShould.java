package uk.ac.newcastle.team22.usb;

import org.junit.Test;

import uk.ac.newcastle.team22.usb.coreUSB.StaffMember;

import static org.junit.Assert.assertEquals;

/**
 * A test class for {@link StaffMember}.
 *
 * @author Patrick Lindley
 * @version 1.0
 */
public class AStaffMemberShould {

    @Test
    /** Assert that a staff member has a first name. */
    public void haveAFirstName() {
        StaffMember newStaffMember = new StaffMember("1234", "Mr", "Patrick", "Lindley",
                "0111111111", "test@ncl.ac.uk");
        assertEquals("Patrick", newStaffMember.getFirstName());
    }

    @Test
    /** Assert that a staff member has a last name. */
    public void haveALastName() {
        StaffMember newStaffMember = new StaffMember("1234", "Mr", "Patrick", "Lindley",
                "0111111111", "test@ncl.ac.uk");
        assertEquals("Lindley", newStaffMember.getLastName());
    }

    @Test
    /** Assert that a staff member has a phone number. */
    public void haveAPhoneNumber() {
        StaffMember newStaffMember = new StaffMember("1234", "Mr", "Patrick", "Lindley",
                "0111111111", "test@ncl.ac.uk");
        assertEquals("0111111111", newStaffMember.getPhoneNumber());
    }

    @Test
    /** Assert that a staff member has an email address. */
    public void haveAnEmailAddress() {
        StaffMember newStaffMember = new StaffMember("1234", "Mr", "Patrick", "Lindley",
                "0111111111", "test@ncl.ac.uk");
        assertEquals("test@ncl.ac.uk", newStaffMember.getEmailAddress());
    }
}
