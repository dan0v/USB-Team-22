package uk.ac.newcastle.team22.usb.coreUSB;

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
    private List<Floor> floors;

    /** The staff members in the Urban Sciences Building. */
    private List<StaffMember> staffMembers;

    /** The café in the Urban Sciences Building. */
    private Cafe cafe;

    /** The map of navigation nodes to their node identifiers. */
    private Map<Integer, Node> navigationNodes;

    /** The opening hours of the Urban Sciences Building. */
    private OpeningHours openingHours;

    /** The out of hours of the Urban Sciences Building. */
    private OpeningHours outOfHours;

    private USBConfiguration configuration;

    /**
     * Constructor using a {@link USBUpdateManager.USBUpdate}.
     * This constructor is used to initialise a {@link USB} from either a cached version of the
     * building or from new data retrieved from Firestore. Both new and cached versions of the
     * building are represented by {@link USBUpdateManager.USBUpdate}. This constructor will usually be called at
     * application launch by {@link USBManager} to initialise the shared {@link USB} instance.
     *
     * See {@link USBUpdateManager.USBUpdate} for more information on Urban Sciences Building
     * updates.
     *
     * @param update The Urban Sciences Building update.
     */
    USB(USBUpdateManager.USBUpdate update) {
        this.floors = update.getFloors();
        this.staffMembers = update.getStaffMembers();
        this.cafe = new Cafe(update);
        this.openingHours = update.getOpeningHours().get(OpeningHours.Service.NORMAL);
        this.outOfHours = update.getOpeningHours().get(OpeningHours.Service.OUT_OF_HOURS);
        this.configuration = update.getConfiguration();

        // Populate map of nodes to their identifiers for faster access during navigation.
        this.navigationNodes = new HashMap<>();
        for (Node node : update.getNavigationNodes()) {
            this.navigationNodes.put(node.getNodeIdentifier(), node);
        }
    }

    /**
     * @return The floors in the Urban Sciences Building.
     */
    public List<Floor> getFloors() {
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

    public int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        int roomsCount = 0;
        for (Floor floor : floors) {
            roomsCount += floor.getRooms().size();
        }
        return "USB (floors: " + floors.size() + ", rooms: " + roomsCount + ")";
    }
}
