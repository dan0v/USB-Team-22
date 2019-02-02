package uk.ac.newcastle.team22.usb.coreUSB;

import java.util.Map;

import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;

/**
 * A class which represents a member of staff.
 *
 * @author Patrick Lindley
 * @author Alexander MacLeod
 * @version 1.0
 */

public class StaffMember implements FirestoreConstructable<StaffMember> {

    private String identifier;

    private String title;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String emailAddress;

    /** Empty constructor. */
    public StaffMember() {}

    @Override
    public StaffMember initFromFirebase(Map<String, Object> firestoreDictionary, String documentIdentifier) {
        String title = (String) firestoreDictionary.get("title");
        String firstName = (String) firestoreDictionary.get("firstName");
        String lastName = (String) firestoreDictionary.get("lastName");
        String phoneNumber = (String) firestoreDictionary.get("phoneNumber");
        String emailAddress = (String) firestoreDictionary.get("emailAddress");

        this.identifier = documentIdentifier;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        return this;
    }

    public StaffMember(String title, String firstName, String lastName, String phoneNumber, String emailAddress) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
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

    public Room getRoom() {
        for (Floor floor : USBManager.shared.getBuilding().getFloors()) {
            for (Room room : floor.getRooms()) {
                if (room.getStaffResidenceIdentifier() == identifier) {
                    return room;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }
}
