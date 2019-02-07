package uk.ac.newcastle.team22.usb.navigation;

import java.util.List;

import uk.ac.newcastle.team22.usb.coreUSB.Room;
import uk.ac.newcastle.team22.usb.coreUSB.USBManager;

/**
 * A class which calculates routes between locations in the Urban Sciences Building.
 * The journey planner obeys constraints such as the user's accessibility requirements.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public class Navigator {

    /**
     * Calculates a route between two locations in the Urban Sciences Building.
     * @param origin The origin of the journey.
     * @param destination The destination of the journey.
     * @param accessibility Boolean value whether the journey needs to include an accessible route.
     * @return The collection of edges to be traversed to reach the destination.
     */
    public List<Edge> getRoute(Room origin, Room destination, boolean accessibility) {
        return getRoute(origin.getNavNode(), destination.getNavNode(), accessibility);
    }

    /**
     * Calculates a route between the entrance of the Urban Sciences Building and another location.
     * @param destination The destination of the journey.
     * @param accessibility Boolean value whether the journey needs to include an accessible route.
     * @return The collection of edges to be traversed to reach the destination.
     */
    public List<Edge> getRoute(Room destination, boolean accessibility) {
        return getRoute(USBManager.shared.getBuilding().getFloors().get(0).getRooms().get(0).getNavNode(), end.getNavNode(), accessibility);
    }

    /**
     * Calculates a route between between two nodes.
     * @param origin The origin node.
     * @param destination The destination node.
     * @param accessibility Boolean value whether the journey needs to include an accessible route.
     * @return The collection of edges to be traversed to reach the destination.
     */
    public List<Edge> getRoute(Node origin, Node destination, boolean accessibility) {

        //TODO add sharedNavNodes field to USB to be pulled from firebase
        //use bruteforce with backtracking
        return null;
    }
}
