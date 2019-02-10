package uk.ac.newcastle.team22.usb.navigation;

import java.util.ArrayList;
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
    private int bestRouteWeight;
    private List<Edge> bestRoute;

    /**
     * Calculates route from start Navigable to end Navigable.
     * @param origin The origin of the journey.
     * @param destination The destination of the journey.
     * @param accessibility Boolean value whether the journey needs to be accessible.
     * @return List of Edges to be traversed to reach destination.
     */
    public List<Edge> getRoute(Navigable origin, Navigable destination, boolean accessibility) {
        return getRoute(origin.getNavNode(), destination.getNavNode(), accessibility);
    }

    /**
     * Calculate route from building entrance if no start position provided.
     * @param destination The origin of the journey.
     * @param accessibility Boolean value whether the journey needs to be accessible.
     * @return List of Edges to be traversed to reach destination.
     */
    public List<Edge> getRoute(Navigable destination, boolean accessibility) {
        return getRoute(USBManager.shared.getBuilding().getNavigationNodes().get(0), destination.getNavNode(), accessibility);
    }

    /**
     * Calculate route from start Node to end Node.
     * @param origin The origin of the journey.
     * @param destination The destination of the journey.
     * @param accessibility Boolean value whether the journey needs to be accessible.
     * @return List of Edges to be traversed to reach the destination.
     */
    public List<Edge> getRoute(Node origin, Node destination, boolean accessibility) {
        bestRouteWeight = Integer.MAX_VALUE;
        bestRoute = new ArrayList<Edge>();

        //find shortest route between origin and destination Nodes using backtracking
        recursiveExplore(origin, destination, origin, origin, accessibility, new ArrayList<Edge>(), Integer.MAX_VALUE);
        return bestRoute;
    }

    /**
     * Recursively explore all Edges of adjacent Nodes, backtracking if the current Edge does not
     * meet requirements. <pre>bestRoute</pre> and <pre>bestRouteWeight</pre> will be updated with
     * the shortest route from the provided origin Node to the provided destination Node which meets
     * accessibility requirements.
     *
     * @param originNode Node to navigate from.
     * @param destinationNode Node to navigate to.
     * @param currentNode Node whose Edges are being explored.
     * @param previousNode Node whose Edge was explored to reach current Node.
     * @param accessibility Ensure all Edges added to route meet accessibility requirements.
     * @param candidateRoute Partially constructed route.
     * @param candidateWeight Weight of partially constructed route.
     */
    private void recursiveExplore(Node originNode, Node destinationNode, Node currentNode, Node previousNode, boolean accessibility, List<Edge> candidateRoute, int candidateWeight) {
        for (Edge currentEdge : currentNode.getEdges()) {
            //ignore Edge if it leads to already explored Node
            if (currentEdge.getDestination().equals(previousNode)) {
                continue;
            }
            //Edge leads out of between start and end Node floors, so go to next Edge
            if (destinationNode.getFloorNumber() > originNode.getFloorNumber() && currentEdge.getDestination().getFloorNumber() < originNode.getFloorNumber() || currentEdge.getDestination().getFloorNumber() > destinationNode.getFloorNumber()) {
                continue;
            }
            //Edge leads out of between start and end Node floors, so go to next Edge
            else if (destinationNode.getFloorNumber() < originNode.getFloorNumber() && currentEdge.getDestination().getFloorNumber() > originNode.getFloorNumber() || currentEdge.getDestination().getFloorNumber() < destinationNode.getFloorNumber()) {
                continue;
            }
            //Edge does not meet accessibility requirements, so go to next Edge
            else if (currentEdge.accessible != accessibility) {
                continue;
            }
            //continuing route would be longer than current best route, so go to next Edge
            else if (candidateWeight + currentEdge.weight >= bestRouteWeight) {
                continue;
            }
            //current shortest route found
            else if (currentEdge.getDestination().equals(destinationNode)) {
                candidateRoute.add(currentEdge);
                bestRoute = new ArrayList<>(candidateRoute);
                bestRouteWeight = candidateWeight+currentEdge.weight;
                //after best route has been updated, remove current Edge from candidate route to move back through recursive call chain and explore along the next Edge
                candidateRoute.remove(currentEdge);
                continue;
            }
            //no backtracking possible, so add Edge to candidate route
            candidateRoute.add(currentEdge);
            //explore along candidate route
            recursiveExplore(originNode, destinationNode, currentEdge.getDestination(), currentNode, accessibility, candidateRoute, (candidateWeight + currentEdge.weight));
            //remove current Edge from candidate route to move back through recursive call chain and explore along the next Edge
            candidateRoute.remove(currentEdge);
        }
        //move back up recursive call chain to explore next Node
        return;
    }
}
