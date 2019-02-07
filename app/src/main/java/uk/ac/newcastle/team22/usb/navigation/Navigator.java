package uk.ac.newcastle.team22.usb.navigation;

import java.util.List;

import uk.ac.newcastle.team22.usb.coreUSB.Room;
import uk.ac.newcastle.team22.usb.coreUSB.USBManager;

/**
 * Find routes between locations within the Urban Sciences Building obeying constraints
 * such as accessibility requirements
 *
 * @author Daniel Vincent
 * @version 1
 */
public class Navigator {

    /**
     * Calculate route from start Node to end Node
     * @param start Room to be navigated from
     * @param end Room to be navigated to
     * @param accessibility will use only lifts to traverse floors
     * @return List of Edges to be traversed to reach destination
     */
    public List<Edge> getRoute(Room start, Room end, boolean accessibility) {
        return getRoute(start.getNavNode(), end.getNavNode(), accessibility);
    }

    /**
     * Calculate route from building entrance if no start position provided
     * @param end Room to be navigated to
     * @param accessibility will use only lifts to traverse floors
     * @return List of Edges to be traversed to reach destination
     */
    public List<Edge> getRoute(Room end, boolean accessibility) {
        return getRoute(USBManager.shared.getBuilding().getFloors().get(0).getRooms().get(0).getNavNode(), end.getNavNode(), accessibility);
    }

    /**
     * Calculate route from start Node to end Node
     * @param start node to be navigated from
     * @param end node to be navigated to
     * @param accessibility will use only lifts to traverse floors
     * @return List of Edges to be traversed to reach destination
     */
    public List<Edge> getRoute(Node start, Node end, boolean accessibility) {

        //TODO add sharedNavNodes field to USB to be pulled from firebase
        //use bruteforce with backtracking
        return null;
    }
}
