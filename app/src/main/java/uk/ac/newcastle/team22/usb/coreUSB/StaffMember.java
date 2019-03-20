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

    /** The unique identifier of the staff member. */
    private String identifier;

    /** The title of the staff member. */
    private String title;

    /** The first name of the staff member. */
    private String firstName;

    /** The last name of the staff member. */
    private String lastName;

    /** The phone number of the staff member. */
    private String phoneNumber;

    /** The email address of the staff member. */
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

    /**
     * @return The title of the staff member.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The first name of the staff member.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return The last name of the staff member.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return The phone number of the staff member.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @return The email address of the staff member.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @return The room of the staff member.
     */
    public Room getRoom() {
        for (Floor floor : USBManager.shared.getBuilding().getFloors().values()) {
            for (Room room : floor.getRooms().values()) {
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
