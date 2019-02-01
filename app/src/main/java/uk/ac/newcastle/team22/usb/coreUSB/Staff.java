package uk.ac.newcastle.team22.usb.coreUSB;

/**
 * A class which represents a member of staff.
 *
 * @author Patrick Lindley
 * @version 1.0
 */

public class Staff {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;
    private Room roomNo;

    public Staff(String firstName, String lastName, String phoneNumber, String emailAddress, Room roomNo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.roomNo = roomNo;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getEmailAddress() {
        return emailAddress;
    }
    public Room getRoomNo() {
        return roomNo;
    }
}
