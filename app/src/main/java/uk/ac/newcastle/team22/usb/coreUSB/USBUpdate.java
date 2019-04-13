package uk.ac.newcastle.team22.usb.coreUSB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.newcastle.team22.usb.navigation.Node;

/**
 * An Urban Sciences Building update.
 * An update may be new information retrieved from Firestore or a cached version of the
 * Urban Sciences Building. A {@link USBUpdate} is used to initialise a {@link USB} object.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class USBUpdate {

    /** The floors in the Urban Sciences Building. */
    private List<Floor> floors = new ArrayList<>();

    /** The staff members in the Urban Sciences Building. */
    private List<StaffMember> staffMembers = new ArrayList<>();

    /** The items, food or drink, which are served at the café in the Urban Sciences Building. */
    private List<CafeMenuItem> cafeMenuItems = new ArrayList<>();

    /** The navigation nodes in the Urban Sciences Building. */
    private List<Node> navigationNodes = new ArrayList<>();

    /** The opening hours in the Urban Sciences Building. */
    private Map<OpeningHours.Service, OpeningHours> openingHours = new HashMap<>();

    /** The configuration of the Urban Sciences Building. */
    private USBConfiguration configuration;

    /** Empty constructor. */
    public USBUpdate() {}

    /**
     * Sets the new floors in the update.
     *
     * @param floors The updated floors.
     */
    public void setFloors(List<Floor> floors) {
        this.floors = floors;
    }

    /**
     * Sets the new staff members in the update.
     *
     * @param staffMembers The updated staff members.
     */
    public void setStaffMembers(List<StaffMember> staffMembers) {
        this.staffMembers = staffMembers;
    }

    /**
     * Sets the new café menu items in the update.
     *
     * @param cafeMenuItems The updated café menu items.
     */
    public void setCafeMenuItems(List<CafeMenuItem> cafeMenuItems) {
        this.cafeMenuItems = cafeMenuItems;
    }

    /**
     * Sets the new navigation nodes in the update.
     *
     * @param nodes The updated nodes.
     */
    public void setNavigationNodes(List<Node> nodes) {
        this.navigationNodes = nodes;
    }

    /**
     * Sets the new opening hours in the update.
     *
     * @param openingHours The updated opening hours.
     */
    public void setOpeningHours(List<OpeningHours> openingHours) {
        for (OpeningHours hours : openingHours) {
            this.openingHours.put(hours.getService(), hours);
        }
    }

    /**
     * Sets the new configuration in the update.
     *
     * @param configuration The updated configuration.
     */
    public void setConfiguration(USBConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * @return The updated floors.
     */
    public List<Floor> getFloors() {
        return floors;
    }

    /**
     * @return The updated staff members.
     */
    public List<StaffMember> getStaffMembers() {
        return staffMembers;
    }

    /**
     * @return The updated café menu items.
     */
    public List<CafeMenuItem> getCafeMenuItems() {
        return cafeMenuItems;
    }

    /**
     * @return The updated navigation nodes.
     */
    public List<Node> getNavigationNodes() {
        return navigationNodes;
    }

    /**
     * @return The updated opening hours nodes.
     */
    public Map<OpeningHours.Service, OpeningHours> getOpeningHours() {
        return openingHours;
    }

    /**
     * @return The updated building configuration.
     */
    public USBConfiguration getConfiguration() { return configuration; }
}