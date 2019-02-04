package uk.ac.newcastle.team22.usb.coreUSB;

import android.annotation.SuppressLint;

import java.util.Map;

import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;
import uk.ac.newcastle.team22.usb.navigation.Node;

/**
 * A class which represents a room in Urban Sciences Building.
 *
 * @author Alexander MacLeod
 * @author Daniel Vincet
 * @version 1.0
 */
public class Room implements FirestoreConstructable<Room> {

    /** The floor on which the room is situated. */
    private Floor floor;

    /** The room number on a given floor. */
    private int number;

    /** The identifier of the staff member who occupies this room */
    private String staffResidenceIdentifier;

    /** The nearest Navigation Node to this Room */
    private Node navNode;

    @Override
    public Room initFromFirebase(Map<String, Object> firestoreDictionary, String documentIdentifier) {
        int number = Integer.parseInt(documentIdentifier);
        String staffResidenceIdentifier = (String) firestoreDictionary.get("staffResidenceIdentifier");
        Node navNode = USBManager.shared.getBuilding().sharedNavNodes.get(Integer.parseInt((String) firestoreDictionary.get("navNode")));

        this.number = number;
        this.staffResidenceIdentifier = staffResidenceIdentifier;
        this.navNode = navNode;
        return this;
    }

    /** Empty constructor. */
    public Room() {}

    /**
     * Helper method to set the floor which this room is situated.
     *
     * @param floor The floor which this room is situated.
     */
    public void attachFloor(Floor floor) {
        this.floor = floor;
    }

    /**
     * @return The floor on which the room is situated.
     */
    public Floor getFloor() {
        return floor;
    }

    /**
     * @return The identifier of the resident staff member.
     */
    public String getStaffResidenceIdentifier() {
        return staffResidenceIdentifier;
    }

    /**
     * @return The room's resident staff member.
     */
    public StaffMember getResidentStaff() {
        for (StaffMember staffMember : USBManager.shared.getBuilding().getStaffMembers()) {
            if (staffMember.getRoom() == this) {
                return staffMember;
            }
        }
        return null;
    }

    /**
     * @return Copy of Room's nearest Nav Node
     */
    public Node getNavNode() {
        return navNode.clone();
    }

    /**
     * Returns the formatted room number.
     * The formatted room number includes the floor number.
     */
    @SuppressLint("DefaultLocale")
    public String getNumber() {
        String floorNumber = String.valueOf(floor.getNumber());
        String roomNumber = String.format("%03d", number);
        return floorNumber + "." + roomNumber;
    }

    @Override
    public String toString() {
        return getNumber();
    }
}