package uk.ac.newcastle.team22.usb.coreUSB;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;
import uk.ac.newcastle.team22.usb.navigation.Navigable;
import uk.ac.newcastle.team22.usb.navigation.Node;
import uk.ac.newcastle.team22.usb.search.ResultReason;
import uk.ac.newcastle.team22.usb.search.Searchable;

/**
 * A class which represents a room in Urban Sciences Building.
 *
 * @author Alexander MacLeod
 * @author Daniel Vincet
 * @version 1.0
 */
public class Room implements FirestoreConstructable<Room>, Navigable, Searchable {

    /** The floor on which the room is situated. */
    private Floor floor;

    /** The room number on a given floor. */
    private String number;

    /** The resources which are available in the room. */
    private List<Resource> resources = new ArrayList();

    /** The identifier of the staff member who occupies the room. */
    private String staffResidenceIdentifier;

    /** The nearest navigation node to the room. */
    private int nodeIdentifier;

    /** The alternate name of the room. */
    private String alternateName;

    public Room(Floor floor, String number, int nodeIdentifier) {
        this.floor = floor;
        this.number = number;
        this.nodeIdentifier = nodeIdentifier;
        floor.attachRoom(this);
    }

    /** Empty constructor. */
    public Room() {}

    @Override
    @SuppressWarnings("unchecked")
    public Room initFromFirebase(Map<String, Object> firestoreDictionary, String documentIdentifier) throws FirestoreConstructable.InitialisationFailed {
        Map<String, Long> resources = (Map<String, Long>) firestoreDictionary.get("resources");
        String staffResidenceIdentifier = (String) firestoreDictionary.get("staffResidenceIdentifier");
        String alternateName = (String) firestoreDictionary.get("roomName");

        this.nodeIdentifier = ((Number) firestoreDictionary.get("nearestNode")).intValue();
        this.number = documentIdentifier;
        this.staffResidenceIdentifier = staffResidenceIdentifier;
        this.alternateName = alternateName;

        // Initialise room resources.
        resources = resources == null ? Collections.emptyMap() : new HashMap();
        for (Map.Entry<String, Long> entry : resources.entrySet()) {
            Resource newResource = new Resource(entry.getKey(), entry.getValue().intValue(), this);
            if (newResource != null) {
                this.resources.add(newResource);
            }
        }
        return this;
    }

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
     * An alternate name of the room may be provided by the manager.
     * For example, the Lecture Theatre.
     * The name of the room will otherwise be its room number.
     *
     * @return The name of the room.
     */
    public String getFormattedName() {
        if (alternateName == null) {
            return "Room " + getFormattedNumber();
        }
        return getAlternateName() + ", " + getFormattedNumber();
    }

    /**
     * @return The formatted room number includes the floor number.
     */
    @SuppressLint("DefaultLocale")
    public String getFormattedNumber() {
        String floorNumber = String.valueOf(floor.getNumber());
        String formattedRoomNumber;
        try {
            int roomNumber = Integer.parseInt(number);
            formattedRoomNumber = String.format("%03d", roomNumber);
        } catch (NumberFormatException e) {
            formattedRoomNumber = "0" + number;
        }
        return floorNumber + "." + formattedRoomNumber;
    }

    /**
     * @return The room number on a given floor.
     */
    public String getNumber() {
        return number;
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
     * Update the number of available computers from JSON file.
     *
     * @param newComputers
     */
    public void updateComputerAvailability(Resource newComputers) {
        for (Resource resource : this.resources) {
            if (resource.getType().equals(ResourceType.COMPUTER)) {
                this.resources.remove(resource);
            }
        }
        this.resources.add(newComputers);
    }

    /**
     * @return The computer resource of this room.
     * Returns a new resource with 0 total and 0 available computers if this room has none.
     */
    public Resource getComputers() {
        for (Resource resource : this.resources) {
            if (resource.getType().equals(ResourceType.COMPUTER)) {
                return resource;
            }
            return new Resource(ResourceType.COMPUTER, 0, 0);
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
    public Node getNavigationNode() {
        return USBManager.shared.getBuilding().getNavigationNodes().get(nodeIdentifier);
    }

    /**
     * @return The alternate name of the room.
     */
    public String getAlternateName() {
        return alternateName;
    }

    @Override
    public List<ResultReason> getSearchableReasons() {
        List<ResultReason> reasons = new ArrayList();

        reasons.add(new ResultReason(number, ResultReason.Reason.ROOM_NUMBER));
        reasons.add(new ResultReason(getFormattedNumber(), ResultReason.Reason.ROOM_FORMATTED_NUMBER));

        if (getAlternateName() != null) {
            reasons.add(new ResultReason(getAlternateName(), ResultReason.Reason.ROOM_ALTERNATE_NAME));
        }

        return reasons;
    }

    @Override
    public String toString() {
        return getFormattedNumber();
    }
}