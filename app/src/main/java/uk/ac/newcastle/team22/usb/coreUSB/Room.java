package uk.ac.newcastle.team22.usb.coreUSB;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;
import uk.ac.newcastle.team22.usb.navigation.Navigable;
import uk.ac.newcastle.team22.usb.navigation.Node;

/**
 * A class which represents a room in Urban Sciences Building.
 *
 * @author Alexander MacLeod
 * @author Daniel Vincet
 * @version 1.0
 */
public class Room implements FirestoreConstructable<Room>, Navigable {

    /** The floor on which the room is situated. */
    private Floor floor;

    /** The room number on a given floor. */
    private int number;

    /** The resources which are available in the room. */
    private List<Resource> resources = new ArrayList<>();

    /** The identifier of the staff member who occupies the room. */
    private String staffResidenceIdentifier;

    /** The nearest Navigation Node to this Room */
    private int nodeIdentifier;

    @Override
    @SuppressWarnings("unchecked")
    public Room initFromFirebase(Map<String, Object> firestoreDictionary, String documentIdentifier) throws FirestoreConstructable.InitialisationFailed {
        int number = Integer.parseInt(documentIdentifier);

        // TODO Temporary check for null node identifier while data is being finalised.
        int nodeIdentifier = -1;
        if (firestoreDictionary.get("node") != null) {
            nodeIdentifier = (int) firestoreDictionary.get("node");
        }

        Map<String, Long> resources = (Map<String, Long>) firestoreDictionary.get("resources");
        String staffResidenceIdentifier = (String) firestoreDictionary.get("staffResidenceIdentifier");

        this.nodeIdentifier = nodeIdentifier;
        this.number = number;
        this.staffResidenceIdentifier = staffResidenceIdentifier;

        // Initialise room resources.
        resources = resources == null ? Collections.<String, Long>emptyMap() : resources;
        for (Map.Entry<String, Long> entry : resources.entrySet()) {
            Resource newResource = new Resource(entry.getKey(), entry.getValue().intValue());
            if (newResource != null) {
                this.resources.add(newResource);
            }
        }
        return this;
    }

    /** Empty constructor. */
    public Room() {}

    /**
     * Helper method to set the floor on which this room is situated.
     *
     * @param floor The floor on which this room is situated.
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
     * Returns the formatted room number.
     * The formatted room number includes the floor number.
     */
    @SuppressLint("DefaultLocale")
    public String getNumber() {
        String floorNumber = String.valueOf(floor.getNumber());
        String roomNumber = String.format("%03d", number);
        return floorNumber + "." + roomNumber;
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
     * @return The resources which are available in the room.
     */
    public List<Resource> getResources() {
        return resources;
    }

    @Override
    public Node getNavNode() {
        return USBManager.shared.getBuilding().getNavigationNodes().get(nodeIdentifier);
    }

    @Override
    public String toString() {
        return getNumber();
    }
}