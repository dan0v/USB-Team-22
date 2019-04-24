package uk.ac.newcastle.team22.usb.coreUSB;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.newcastle.team22.usb.navigation.Node;

/**
 * A class which represents the Urban Sciences Building.
 *
 * @author Daniel Vincent
 * @author Alexander MacLeod
 * @version 1.0
 */
public class USB {

    /** The floors in the Urban Sciences Building. */
    private Map<Integer, Floor> floors;

    /** The staff members in the Urban Sciences Building. */
    private List<StaffMember> staffMembers;

    /** The café in the Urban Sciences Building. */
    private Cafe cafe;

    /** The Map of navigation Nodes to their Node identifiers. */
    private Map<Integer, Node> navigationNodes;

    /** The list of Nodes identifiers of Nodes along the guided tour route. */
    private List<Integer> tourNodeIdentifiers;

    /** The opening hours of the Urban Sciences Building. */
    private OpeningHours openingHours;

    /** The out of hours of the Urban Sciences Building. */
    private OpeningHours outOfHours;

    /** The configuration of the Urban Sciences Building. */
    private USBConfiguration configuration;

    /** The last time a successful JSON update was executed. */
    private Calendar lastJSONUpdate;

    /**
     * Constructor using a {@link USBUpdate}.
     * This constructor is used to initialise a {@link USB} from either a cached version of the
     * building or from new data retrieved from Firestore. Both new and cached versions of the
     * building are represented by {@link USBUpdate}. This constructor will usually be called at
     * application launch by {@link USBManager} to initialise the shared {@link USB} instance.
     *
     * See {@link USBUpdate} for more information on Urban Sciences Building
     * updates.
     *
     * @param update The Urban Sciences Building update.
     */
    public USB(USBUpdate update) {
        this.staffMembers = update.getStaffMembers();
        this.cafe = new Cafe(update);
        this.openingHours = update.getOpeningHours().get(OpeningHours.Service.NORMAL);
        this.outOfHours = update.getOpeningHours().get(OpeningHours.Service.OUT_OF_HOURS);
        this.configuration = update.getConfiguration();

        // Populate map of floors to their floor numbers for faster.
        this.floors = new HashMap<>();
        for (Floor floor : update.getFloors()) {
            this.floors.put(floor.getNumber(), floor);
        }

        // Populate map of nodes to their identifiers for faster access during navigation.
        this.navigationNodes = new HashMap<>();
        this.tourNodeIdentifiers = new ArrayList<>();

        for (Node node : update.getNavigationNodes()) {
            this.navigationNodes.put(node.getNodeIdentifier(), node);
            if (node.isTourNode()) {
                tourNodeIdentifiers.add(node.getNodeIdentifier());
            }
        }
        // Ensure tour nodes are ordered correctly.
        Collections.sort(this.tourNodeIdentifiers);
    }

    /**
     * @return The floors in the Urban Sciences Building.
     */
    public Map<Integer, Floor> getFloors() {
        return this.floors;
    }

    /**
     * @return The staff members in the Urban Sciences Building.
     */
    public List<StaffMember> getStaffMembers() {
        return staffMembers;
    }

    /**
     * @return The café at the Urban Sciences Building.
     */
    public Cafe getCafe() {
        return cafe;
    }

    /**
     * @return The navigation nodes in the Urban Sciences Building.
     */
    public Map<Integer, Node> getNavigationNodes() {
        return this.navigationNodes;
    }

    /**
     * @return The identifiers of tour nodes in the Urban Sciences Building.
     */
    public List<Integer> getTourNodeIdentifiers() {
        return this.tourNodeIdentifiers;
    }

    /**
     * @return The opening hours of the Urban Sciences Building.
     */
    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    /**
     * @return The out of hours of the Urban Sciences Building.
     */
    public OpeningHours getOutOfHours() {
        return outOfHours;
    }

    /**
     * @return The time of the last known update to computer availability data.
     */
    public Calendar getLastJSONUpdateTime() {
        return this.lastJSONUpdate;
    }

    /**
     * @return The time of the last known update to computer availability data.
     */
    public void setLastJSONUpdateTime(Calendar newUpdate) {
        this.lastJSONUpdate = newUpdate;
    }

    /**
     * @return The configuration of the Urban Sciences Building.
     */
    public USBConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public String toString() {
        int roomsCount = 0;
        for (Floor floor : floors.values()) {
            roomsCount += floor.getRooms().size();
        }
        return "USB (floors: " + floors.size() + ", rooms: " + roomsCount + ")";
    }
}
